package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Factura;
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
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar factura por id
    @Override
    Optional<Factura> findById(Integer idFactura);

    // 2) Listar todas las facturas
    @Override
    List<Factura> findAll();

    // 3) Listar facturas de un cliente específico (por id_cliente / DNI)
    List<Factura> findByCliente_IdCliente(String idCliente);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar facturas por estado (id_estado_factura)
    List<Factura> findByEstadoFactura_IdEstadoFactura(Byte idEstadoFactura);

    // 5) Listar facturas en un rango de fechas de creación
    List<Factura> findByFechaCreacionBetween(LocalDate inicio, LocalDate fin);

    // 6) Listar facturas cuyo total esté en un rango
    List<Factura> findByTotalBetween(BigDecimal totalMin, BigDecimal totalMax);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - facturas (esta)
    //  - clientes (id_cliente, nombre)
    //  - estado_factura (id_estado_factura, estado)
    //  - orden_trabajo (id_orden_trabajo, fecha_ingreso)

    // 7) Resumen de facturación por cliente en un rango de fechas
    //    Devuelve filas:
    //      [idCliente, nombreCliente, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal, cantidadFacturas]
    @Query(value = """
            SELECT c.id_cliente                                   AS idCliente,
                   c.nombre                                       AS nombreCliente,
                   COALESCE(SUM(f.total), 0)                      AS totalFacturado,
                   COALESCE(SUM(f.sub_total_mano_de_obra), 0)     AS manoObraTotal,
                   COALESCE(SUM(f.sub_total_repuestos), 0)        AS repuestosTotal,
                   COALESCE(SUM(f.impuesto), 0)                   AS impuestoTotal,
                   COUNT(f.id_factura)                            AS cantidadFacturas
            FROM clientes c
            JOIN facturas f
                 ON f.id_cliente = c.id_cliente
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY c.id_cliente, c.nombre
            ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<Object[]> resumenFacturacionPorClienteEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // 8) Total facturado por estado de factura en un rango de fechas
    //    Devuelve filas: [idEstadoFactura, nombreEstado, totalFacturado, cantidadFacturas]
    @Query(value = """
            SELECT ef.id_estado_factura                         AS idEstadoFactura,
                   ef.estado                                    AS nombreEstado,
                   COALESCE(SUM(f.total), 0)                    AS totalFacturado,
                   COUNT(f.id_factura)                          AS cantidadFacturas
            FROM estado_factura ef
            JOIN facturas f
                 ON f.id_estado_factura = ef.id_estado_factura
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY ef.id_estado_factura, ef.estado
            ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<Object[]> totalFacturadoPorEstadoEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // 9) Facturación mensual global en un rango de fechas
    //    Devuelve filas:
    //      [anio, mes, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal, cantidadFacturas]
    @Query(value = """
            SELECT YEAR(f.fecha_creacion)                        AS anio,
                   MONTH(f.fecha_creacion)                       AS mes,
                   COALESCE(SUM(f.total), 0)                     AS totalFacturado,
                   COALESCE(SUM(f.sub_total_mano_de_obra), 0)    AS manoObraTotal,
                   COALESCE(SUM(f.sub_total_repuestos), 0)       AS repuestosTotal,
                   COALESCE(SUM(f.impuesto), 0)                  AS impuestoTotal,
                   COUNT(f.id_factura)                           AS cantidadFacturas
            FROM facturas f
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY YEAR(f.fecha_creacion), MONTH(f.fecha_creacion)
            ORDER BY anio, mes
            """, nativeQuery = true)
    List<Object[]> facturacionMensualEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
