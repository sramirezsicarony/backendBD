package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<Especialidad> findById(Integer idEspecialidad);

    // 2) Listar todas las especialidades
    @Override
    List<Especialidad> findAll();

    // 3) Buscar una especialidad por nombre exacto
    Especialidad findByNombreEspecialidad(String nombreEspecialidad);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar especialidades cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Especialidad> findByNombreEspecialidadContainingIgnoreCase(String texto);

    // 5) Buscar especialidades creadas en un rango de fechas
    List<Especialidad> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar especialidades ordenadas alfabéticamente por nombre
    List<Especialidad> findAllByOrderByNombreEspecialidadAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Usan tablas reales:
    //  - mecanicos (id_especialidad FK -> especialidades)
    //  - orden_trabajo_mecanico (id_mecanico FK -> mecanicos)

    // 7) Contar cuántos mecánicos tiene cada especialidad
    //    Devuelve filas: [idEspecialidad, nombreEspecialidad, cantidadMecanicos]
    @Query(value = """
            SELECT e.id_especialidad                 AS idEspecialidad,
                   e.nombre_especialidad             AS nombreEspecialidad,
                   COUNT(m.id_mecanico)              AS cantidadMecanicos
            FROM especialidades e
            LEFT JOIN mecanicos m
                   ON m.id_especialidad = e.id_especialidad
            GROUP BY e.id_especialidad, e.nombre_especialidad
            ORDER BY cantidadMecanicos DESC
            """, nativeQuery = true)
    List<Object[]> contarMecanicosPorEspecialidad();

    // 8) Especialidades que NO tienen ningún mecánico asignado
    @Query(value = """
            SELECT e.*
            FROM especialidades e
            LEFT JOIN mecanicos m
                   ON m.id_especialidad = e.id_especialidad
            WHERE m.id_mecanico IS NULL
            """, nativeQuery = true)
    List<Especialidad> findEspecialidadesSinMecanicos();

    // 9) Costo total de mano de obra por especialidad en un rango de fechas
    //    (según created_at de orden_trabajo_mecanico)
    //    Devuelve filas: [idEspecialidad, nombreEspecialidad, costoTotalManoObra]
    @Query(value = """
            SELECT e.id_especialidad                 AS idEspecialidad,
                   e.nombre_especialidad             AS nombreEspecialidad,
                   COALESCE(SUM(otm.costo_total), 0) AS costoTotalManoObra
            FROM especialidades e
            JOIN mecanicos m
                 ON m.id_especialidad = e.id_especialidad
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_mecanico = m.id_mecanico
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY e.id_especialidad, e.nombre_especialidad
            ORDER BY costoTotalManoObra DESC
            """, nativeQuery = true)
    List<Object[]> costoTotalManoObraPorEspecialidadEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
