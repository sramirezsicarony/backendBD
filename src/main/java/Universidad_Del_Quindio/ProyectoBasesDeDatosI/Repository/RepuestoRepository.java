package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar repuesto por id
    @Override
    Optional<Repuesto> findById(Integer idRepuesto);

    // 2) Listar todos los repuestos
    @Override
    List<Repuesto> findAll();

    // 3) Buscar repuesto por nombre exacto
    Repuesto findByNombre(String nombre);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar repuestos cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Repuesto> findByNombreContainingIgnoreCase(String texto);

    // 5) Buscar repuestos por categoría (id de la categoría)
    List<Repuesto> findByCategoria_IdCategoria(Integer idCategoria);

    // 6) Buscar repuestos creados en un rango de fechas
    List<Repuesto> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - bodega (id_repuesto FK -> repuestos, stock, precio_venta)
    //  - detalle_orden_repuesto (id_repuesto FK -> repuestos, cantidad, sub_total, created_at)
    //  - suministra (id_repuesto FK -> repuestos, cantidad, costo_total, fecha_ingreso)

    // 7) Stock total y valor total en todos los almacenes por repuesto
    //    Devuelve filas: [idRepuesto, nombreRepuesto, stockTotal, valorTotal]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(b.stock), 0)              AS stockTotal,
                   COALESCE(SUM(b.stock * b.precio_venta), 0) AS valorTotal
            FROM repuestos r
            LEFT JOIN bodega b
                   ON b.id_repuesto = r.id_repuesto
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY valorTotal DESC
            """, nativeQuery = true)
    List<Object[]> stockYValorTotalPorRepuesto();

    // 8) Total vendido por repuesto (según detalle_orden_repuesto) en un rango de fechas
    //    Usa created_at de detalle_orden_repuesto
    //    Devuelve filas: [idRepuesto, nombreRepuesto, cantidadTotal, totalVendido]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(dor.cantidad), 0)          AS cantidadTotal,
                   COALESCE(SUM(dor.sub_total), 0)         AS totalVendido
            FROM repuestos r
            JOIN detalle_orden_repuesto dor
                 ON dor.id_repuesto = r.id_repuesto
            WHERE dor.created_at BETWEEN :inicio AND :fin
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY totalVendido DESC
            """, nativeQuery = true)
    List<Object[]> ventasPorRepuestoEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // 9) Total comprado por repuesto (según suministra) en un rango de fechas
    //    Usa fecha_ingreso de suministra
    //    Devuelve filas: [idRepuesto, nombreRepuesto, cantidadComprada, costoTotalCompras]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(s.cantidad), 0)            AS cantidadComprada,
                   COALESCE(SUM(s.costo_total), 0)         AS costoTotalCompras
            FROM repuestos r
            JOIN suministra s
                 ON s.id_repuesto = r.id_repuesto
            WHERE s.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY costoTotalCompras DESC
            """, nativeQuery = true)
    List<Object[]> comprasPorRepuestoEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
