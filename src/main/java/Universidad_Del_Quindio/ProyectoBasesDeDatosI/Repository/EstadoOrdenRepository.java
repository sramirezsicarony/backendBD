package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden, Byte> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<EstadoOrden> findById(Byte idEstadoOrden);

    // 2) Listar todos los estados de orden
    @Override
    List<EstadoOrden> findAll();

    // 3) Buscar un estado por su nombre exacto
    EstadoOrden findByEstado(String estado);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar estados cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<EstadoOrden> findByEstadoContainingIgnoreCase(String texto);

    // 5) Buscar estados creados en un rango de fechas
    List<EstadoOrden> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar estados ordenados alfabéticamente por nombre
    List<EstadoOrden> findAllByOrderByEstadoAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Usan tabla real:
    //  - orden_trabajo (id_estado_orden FK -> estados_orden)

    // 7) Contar cuántas órdenes hay por cada estado
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

    // 8) Estados que NO tienen ninguna orden asociada
    @Query(value = """
            SELECT eo.*
            FROM estados_orden eo
            LEFT JOIN orden_trabajo ot
                   ON ot.id_estado_orden = eo.id_estado_orden
            WHERE ot.id_orden_trabajo IS NULL
            """, nativeQuery = true)
    List<EstadoOrden> findEstadosSinOrdenes();

    // 9) Promedio de días que duran las órdenes en cada estado
    //    (solo considera órdenes con fecha_salida NO nula)
    //    Devuelve filas: [idEstadoOrden, nombreEstado, promedioDias]
    @Query(value = """
            SELECT eo.id_estado_orden                                       AS idEstadoOrden,
                   eo.estado                                                 AS nombreEstado,
                   AVG(TIMESTAMPDIFF(DAY, ot.fecha_ingreso, ot.fecha_salida)) AS promedioDias
            FROM estados_orden eo
            JOIN orden_trabajo ot
                 ON ot.id_estado_orden = eo.id_estado_orden
            WHERE ot.fecha_salida IS NOT NULL
              AND ot.fecha_ingreso IS NOT NULL
            GROUP BY eo.id_estado_orden, eo.estado
            ORDER BY promedioDias DESC
            """, nativeQuery = true)
    List<Object[]> promedioDiasPorEstado();

    // Variante con filtro por rango de fechas de ingreso (opcional, si la necesitas):
    // Promedio de días por estado, solo considerando órdenes cuyo ingreso esté entre dos fechas.
    @Query(value = """
            SELECT eo.id_estado_orden                                       AS idEstadoOrden,
                   eo.estado                                                 AS nombreEstado,
                   AVG(TIMESTAMPDIFF(DAY, ot.fecha_ingreso, ot.fecha_salida)) AS promedioDias
            FROM estados_orden eo
            JOIN orden_trabajo ot
                 ON ot.id_estado_orden = eo.id_estado_orden
            WHERE ot.fecha_salida IS NOT NULL
              AND ot.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY eo.id_estado_orden, eo.estado
            ORDER BY promedioDias DESC
            """, nativeQuery = true)
    List<Object[]> promedioDiasPorEstadoEnRango(
            @Param("inicio") java.sql.Date inicio,
            @Param("fin") java.sql.Date fin
    );

}
