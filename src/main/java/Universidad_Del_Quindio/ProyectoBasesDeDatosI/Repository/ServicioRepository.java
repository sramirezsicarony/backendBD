package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<Servicio> findById(Integer idServicio);

    // 2) Listar todos los servicios
    @Override
    List<Servicio> findAll();

    // 3) Buscar un servicio por su nombre exacto
    Servicio findByServicio(String servicio);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar servicios cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Servicio> findByServicioContainingIgnoreCase(String texto);

    // 5) Buscar servicios creados en un rango de fechas
    List<Servicio> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar servicios ordenados alfabéticamente por nombre
    List<Servicio> findAllByOrderByServicioAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - vehiculo_servicio_orden_trabajo (id_servicio FK -> servicios)
    //    campos: id_vehiculo, id_servicio, id_orden_trabajo, fecha_de_ejecucion

    // 7) Veces que se ha ejecutado cada servicio
    //    Devuelve filas: [idServicio, nombreServicio, cantidadEjecuciones]
    @Query(value = """
            SELECT s.id_servicio                  AS idServicio,
                   s.servicio                     AS nombreServicio,
                   COUNT(vso.id_vehiculo_servicio_orden_trabajo) AS cantidadEjecuciones
            FROM servicios s
            LEFT JOIN vehiculo_servicio_orden_trabajo vso
                   ON vso.id_servicio = s.id_servicio
            GROUP BY s.id_servicio, s.servicio
            ORDER BY cantidadEjecuciones DESC
            """, nativeQuery = true)
    List<Object[]> contarEjecucionesPorServicio();

    // 8) Servicios que nunca se han ejecutado (ningún registro en vehiculo_servicio_orden_trabajo)
    @Query(value = """
            SELECT s.*
            FROM servicios s
            LEFT JOIN vehiculo_servicio_orden_trabajo vso
                   ON vso.id_servicio = s.id_servicio
            WHERE vso.id_vehiculo_servicio_orden_trabajo IS NULL
            """, nativeQuery = true)
    List<Servicio> findServiciosNuncaEjecutados();

    // 9) Uso de servicios en un rango de fechas de ejecución
    //    Devuelve filas: [idServicio, nombreServicio, ejecuciones, vehiculosDistintos]
    @Query(value = """
            SELECT s.id_servicio                                                AS idServicio,
                   s.servicio                                                   AS nombreServicio,
                   COUNT(vso.id_vehiculo_servicio_orden_trabajo)                AS ejecuciones,
                   COUNT(DISTINCT vso.id_vehiculo)                              AS vehiculosDistintos
            FROM servicios s
            JOIN vehiculo_servicio_orden_trabajo vso
                 ON vso.id_servicio = s.id_servicio
            WHERE vso.fecha_de_ejecucion BETWEEN :inicio AND :fin
            GROUP BY s.id_servicio, s.servicio
            ORDER BY ejecuciones DESC
            """, nativeQuery = true)
    List<Object[]> usoDeServiciosEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
