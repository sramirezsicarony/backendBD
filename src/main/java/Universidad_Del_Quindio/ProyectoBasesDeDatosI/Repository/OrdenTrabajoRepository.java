package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar orden por id
    @Override
    Optional<OrdenTrabajo> findById(Integer idOrdenTrabajo);

    // 2) Listar todas las órdenes de trabajo
    @Override
    List<OrdenTrabajo> findAll();

    // 3) Listar órdenes de un vehículo (por placa)
    List<OrdenTrabajo> findByVehiculo_IdVehiculo(String idVehiculo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar órdenes por estado (id_estado_orden)
    List<OrdenTrabajo> findByEstadoOrden_IdEstadoOrden(Byte idEstadoOrden);

    // 5) Listar órdenes por rango de fechas de ingreso
    List<OrdenTrabajo> findByFechaIngresoBetween(LocalDate inicio, LocalDate fin);

    // 6) Listar órdenes que aún no tienen fecha de salida (abiertas)
    List<OrdenTrabajo> findByFechaSalidaIsNull();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - vehiculos (id_vehiculo, id_cliente)
    //  - clientes (id_cliente, nombre)
    //  - estados_orden (id_estado_orden, estado)
    //  - facturas (id_orden_trabajo, fecha_creacion, sub_total_mano_de_obra, sub_total_repuestos, impuesto, total)

    // 7) Cantidad de órdenes por estado
    //    Devuelve filas: [idEstadoOrden, nombreEstado, cantidadOrdenes]
    @Query(value = """
            SELECT eo.id_estado_orden                AS idEstadoOrden,
                   eo.estado                          AS nombreEstado,
                   COUNT(ot.id_orden_trabajo)         AS cantidadOrdenes
            FROM estados_orden eo
            LEFT JOIN orden_trabajo ot
                   ON ot.id_estado_orden = eo.id_estado_orden
            GROUP BY eo.id_estado_orden, eo.estado
            ORDER BY cantidadOrdenes DESC
            """, nativeQuery = true)
    List<Object[]> contarOrdenesPorEstado();

    // 8) Cantidad de órdenes por cliente en un rango de fechas de ingreso
    //    Devuelve filas: [idCliente, nombreCliente, cantidadOrdenes]
    @Query(value = """
            SELECT c.id_cliente                    AS idCliente,
                   c.nombre                        AS nombreCliente,
                   COUNT(ot.id_orden_trabajo)      AS cantidadOrdenes
            FROM orden_trabajo ot
            JOIN vehiculos v
                 ON v.id_vehiculo = ot.id_vehiculo
            JOIN clientes c
                 ON c.id_cliente = v.id_cliente
            WHERE ot.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY c.id_cliente, c.nombre
            ORDER BY cantidadOrdenes DESC
            """, nativeQuery = true)
    List<Object[]> contarOrdenesPorClienteEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // 9) Resumen de costos por orden de trabajo (según facturas) en un rango de fechas de factura
    //    Devuelve filas: [idOrdenTrabajo, fechaIngreso, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal, cantidadFacturas]
    @Query(value = """
            SELECT ot.id_orden_trabajo                              AS idOrdenTrabajo,
                   ot.fecha_ingreso                                 AS fechaIngreso,
                   COALESCE(SUM(f.total), 0)                        AS totalFacturado,
                   COALESCE(SUM(f.sub_total_mano_de_obra), 0)       AS manoObraTotal,
                   COALESCE(SUM(f.sub_total_repuestos), 0)          AS repuestosTotal,
                   COALESCE(SUM(f.impuesto), 0)                     AS impuestoTotal,
                   COUNT(f.id_factura)                              AS cantidadFacturas
            FROM orden_trabajo ot
            LEFT JOIN facturas f
                   ON f.id_orden_trabajo = ot.id_orden_trabajo
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY ot.id_orden_trabajo, ot.fecha_ingreso
            ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<Object[]> resumenCostosPorOrdenEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
