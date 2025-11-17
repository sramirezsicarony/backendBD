package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Suministra;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.SuministraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuministraRepository extends JpaRepository<Suministra, SuministraId> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar registro de suministro por ID compuesto (id_proveedor, id_repuesto)
    @Override
    Optional<Suministra> findById(SuministraId id);

    // 2) Listar todos los registros de suministros
    @Override
    List<Suministra> findAll();

    // 3) Listar todos los suministros de un proveedor específico
    List<Suministra> findByProveedor_IdProveedor(Integer idProveedor);

    // (extra muy útil) Listar todos los suministros de un repuesto específico
    List<Suministra> findByRepuesto_IdRepuesto(Integer idRepuesto);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Suministros en un rango de fechas de ingreso
    List<Suministra> findByFechaIngresoBetween(LocalDate inicio, LocalDate fin);

    // 5) Suministros cuyo costo unitario está en un rango
    List<Suministra> findByCostoUnitarioBetween(BigDecimal costoMin, BigDecimal costoMax);

    // 6) Suministros cuya cantidad es mayor o igual a un valor
    List<Suministra> findByCantidadGreaterThanEqual(Integer cantidadMinima);

    // (extra por fecha de creación) Suministros creados en un rango de timestamps
    List<Suministra> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales:
    //  - suministra (esta)
    //  - proveedores (id_proveedor, nombre)
    //  - repuestos (id_repuesto, nombre)

    // 7) Resumen de compras por proveedor (global)
    //    Devuelve filas: [idProveedor, nombreProveedor, cantidadTotal, costoTotal]
    @Query(value = """
            SELECT p.id_proveedor                     AS idProveedor,
                   p.nombre                           AS nombreProveedor,
                   COALESCE(SUM(s.cantidad), 0)       AS cantidadTotal,
                   COALESCE(SUM(s.costo_total), 0)    AS costoTotal
            FROM proveedores p
            JOIN suministra s
                 ON s.id_proveedor = p.id_proveedor
            GROUP BY p.id_proveedor, p.nombre
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> resumenComprasPorProveedor();

    // 8) Resumen de compras por repuesto (global)
    //    Devuelve filas: [idRepuesto, nombreRepuesto, cantidadTotal, costoTotal]
    @Query(value = """
            SELECT r.id_repuesto                      AS idRepuesto,
                   r.nombre                           AS nombreRepuesto,
                   COALESCE(SUM(s.cantidad), 0)       AS cantidadTotal,
                   COALESCE(SUM(s.costo_total), 0)    AS costoTotal
            FROM repuestos r
            JOIN suministra s
                 ON s.id_repuesto = r.id_repuesto
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY cantidadTotal DESC
            """, nativeQuery = true)
    List<Object[]> resumenComprasPorRepuesto();

    // 9) Compras por proveedor en un rango de fechas de ingreso
    //    Devuelve filas: [idProveedor, nombreProveedor, cantidadTotal, costoTotal]
    @Query(value = """
            SELECT p.id_proveedor                     AS idProveedor,
                   p.nombre                           AS nombreProveedor,
                   COALESCE(SUM(s.cantidad), 0)       AS cantidadTotal,
                   COALESCE(SUM(s.costo_total), 0)    AS costoTotal
            FROM proveedores p
            JOIN suministra s
                 ON s.id_proveedor = p.id_proveedor
            WHERE s.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY p.id_proveedor, p.nombre
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> comprasPorProveedorEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
