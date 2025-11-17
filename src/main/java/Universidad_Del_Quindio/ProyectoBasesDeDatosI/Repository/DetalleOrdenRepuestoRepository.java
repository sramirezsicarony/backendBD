package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuestoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleOrdenRepuestoRepository extends JpaRepository<DetalleOrdenRepuesto, DetalleOrdenRepuestoId> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar detalle por ID compuesto (id_orden_trabajo, id_repuesto)
    @Override
    Optional<DetalleOrdenRepuesto> findById(DetalleOrdenRepuestoId id);

    // 2) Listar todos los detalles de repuestos
    @Override
    List<DetalleOrdenRepuesto> findAll();

    // 3) Listar todos los detalles de una orden específica
    List<DetalleOrdenRepuesto> findByOrdenTrabajo_IdOrdenTrabajo(Integer idOrdenTrabajo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar todos los detalles de un repuesto específico
    List<DetalleOrdenRepuesto> findByRepuesto_IdRepuesto(Integer idRepuesto);

    // 5) Detalles con cantidad mayor o igual a un valor
    List<DetalleOrdenRepuesto> findByCantidadGreaterThanEqual(Short cantidadMinima);

    // 6) Detalles creados en un rango de fechas (created_at)
    List<DetalleOrdenRepuesto> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales:
    //  - detalle_orden_repuesto (esta)
    //  - orden_trabajo (id_orden_trabajo, fecha_ingreso, id_vehiculo)
    //  - vehiculos (id_vehiculo, id_cliente)
    //  - clientes (id_cliente, nombre)
    //  - repuestos (id_repuesto, nombre)

    // 7) Total de repuestos y total $ por orden de trabajo
    //    Devuelve filas: [idOrdenTrabajo, cantidadTotal, totalRepuestos]
    @Query(value = """
            SELECT dor.id_orden_trabajo                    AS idOrdenTrabajo,
                   COALESCE(SUM(dor.cantidad), 0)          AS cantidadTotal,
                   COALESCE(SUM(dor.sub_total), 0)         AS totalRepuestos
            FROM detalle_orden_repuesto dor
            GROUP BY dor.id_orden_trabajo
            ORDER BY totalRepuestos DESC
            """, nativeQuery = true)
    List<Object[]> resumenRepuestosPorOrden();

    // 8) Repuestos más utilizados (por cantidad total y valor total)
    //    Devuelve filas: [idRepuesto, nombreRepuesto, cantidadTotal, totalRepuestos]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(dor.cantidad), 0)          AS cantidadTotal,
                   COALESCE(SUM(dor.sub_total), 0)         AS totalRepuestos
            FROM repuestos r
            JOIN detalle_orden_repuesto dor
                 ON dor.id_repuesto = r.id_repuesto
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY cantidadTotal DESC
            """, nativeQuery = true)
    List<Object[]> repuestosMasUtilizados();

    // 9) Consumo de repuestos por cliente en un rango de fechas de ingreso de la orden
    //    Devuelve filas: [idCliente, nombreCliente, cantidadTotal, totalRepuestos]
    @Query(value = """
            SELECT c.id_cliente                             AS idCliente,
                   c.nombre                                 AS nombreCliente,
                   COALESCE(SUM(dor.cantidad), 0)           AS cantidadTotal,
                   COALESCE(SUM(dor.sub_total), 0)          AS totalRepuestos
            FROM clientes c
            JOIN vehiculos v
                 ON v.id_cliente = c.id_cliente
            JOIN orden_trabajo ot
                 ON ot.id_vehiculo = v.id_vehiculo
            JOIN detalle_orden_repuesto dor
                 ON dor.id_orden_trabajo = ot.id_orden_trabajo
            WHERE ot.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY c.id_cliente, c.nombre
            ORDER BY totalRepuestos DESC
            """, nativeQuery = true)
    List<Object[]> consumoRepuestosPorClienteEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
