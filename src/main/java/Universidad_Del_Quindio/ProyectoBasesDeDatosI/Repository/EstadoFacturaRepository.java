package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoFacturaRepository extends JpaRepository<EstadoFactura, Byte> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<EstadoFactura> findById(Byte idEstadoFactura);

    // 2) Listar todos los estados de factura
    @Override
    List<EstadoFactura> findAll();

    // 3) Buscar un estado por su nombre exacto
    EstadoFactura findByEstado(String estado);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar estados cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<EstadoFactura> findByEstadoContainingIgnoreCase(String texto);

    // 5) Buscar estados creados en un rango de fechas (created_at)
    List<EstadoFactura> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar estados de factura ordenados alfabéticamente por nombre
    List<EstadoFactura> findAllByOrderByEstadoAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Usan tabla real:
    //  - facturas (id_estado_factura FK -> estado_factura)

    // 7) Contar cuántas facturas hay por cada estado
    //    Devuelve filas: [idEstadoFactura, nombreEstado, cantidadFacturas]
    @Query(value = """
            SELECT ef.id_estado_factura              AS idEstadoFactura,
                   ef.estado                         AS nombreEstado,
                   COUNT(f.id_factura)               AS cantidadFacturas
            FROM estado_factura ef
            LEFT JOIN facturas f
                   ON f.id_estado_factura = ef.id_estado_factura
            GROUP BY ef.id_estado_factura, ef.estado
            ORDER BY cantidadFacturas DESC
            """, nativeQuery = true)
    List<Object[]> contarFacturasPorEstado();

    // 8) Estados de factura que NO tienen ninguna factura asociada
    @Query(value = """
            SELECT ef.*
            FROM estado_factura ef
            LEFT JOIN facturas f
                   ON f.id_estado_factura = ef.id_estado_factura
            WHERE f.id_factura IS NULL
            """, nativeQuery = true)
    List<EstadoFactura> findEstadosSinFacturas();

    // 9) Total facturado (SUM(total)) por estado en un rango de fechas de creación
    //    (usa columna fecha_creacion de facturas)
    //    Devuelve filas: [idEstadoFactura, nombreEstado, totalFacturado]
    @Query(value = """
            SELECT ef.id_estado_factura              AS idEstadoFactura,
                   ef.estado                         AS nombreEstado,
                   COALESCE(SUM(f.total), 0)         AS totalFacturado
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

}
