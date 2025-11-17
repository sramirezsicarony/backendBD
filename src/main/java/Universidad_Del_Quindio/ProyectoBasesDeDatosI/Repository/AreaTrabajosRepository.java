package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajos;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AreaTrabajosRepository extends JpaRepository<AreaTrabajos, AreaTrabajosId> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar registro por ID compuesto (id_area_laboral, id_mecanico)
    @Override
    Optional<AreaTrabajos> findById(AreaTrabajosId id);

    // 2) Listar todas las asignaciones área–mecánico
    @Override
    List<AreaTrabajos> findAll();

    // 3) Listar todas las asignaciones de una área laboral específica
    List<AreaTrabajos> findByAreaLaboral_IdAreaLaboral(Integer idAreaLaboral);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar todas las asignaciones de un mecánico específico
    List<AreaTrabajos> findByMecanico_IdMecanico(String idMecanico);

    // 5) Asignaciones creadas en un rango de fechas (created_at)
    List<AreaTrabajos> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Asignaciones donde el nombre del área contiene cierto texto (JOIN implícito por la relación)
    List<AreaTrabajos> findByAreaLaboral_AreaContainingIgnoreCase(String texto);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales:
    //  - area_trabajos (esta)
    //  - area_laboral (id_area_laboral, area)
    //  - mecanicos (id_mecanico, experiencia, id_especialidad)
    //  - orden_trabajo_mecanico (id_mecanico, horas, costo_total, created_at)

    // 7) Cantidad de mecánicos asignados por área laboral
    //    Devuelve filas: [idAreaLaboral, nombreArea, cantidadMecanicos]
    @Query(value = """
            SELECT al.id_area_laboral                       AS idAreaLaboral,
                   al.area                                  AS nombreArea,
                   COALESCE(COUNT(DISTINCT at.id_mecanico), 0) AS cantidadMecanicos
            FROM area_laboral al
            LEFT JOIN area_trabajos at
                   ON at.id_area_laboral = al.id_area_laboral
            GROUP BY al.id_area_laboral, al.area
            ORDER BY cantidadMecanicos DESC
            """, nativeQuery = true)
    List<Object[]> contarMecanicosPorAreaLaboral();

    // 8) Cantidad de áreas laborales en las que trabaja cada mecánico
    //    Devuelve filas: [idMecanico, cantidadAreas]
    @Query(value = """
            SELECT at.id_mecanico                           AS idMecanico,
                   COALESCE(COUNT(DISTINCT at.id_area_laboral), 0) AS cantidadAreas
            FROM area_trabajos at
            GROUP BY at.id_mecanico
            ORDER BY cantidadAreas DESC
            """, nativeQuery = true)
    List<Object[]> contarAreasPorMecanico();

    // 9) Horas totales trabajadas por área laboral en un rango de fechas
    //    Join: area_laboral -> area_trabajos -> mecanicos -> orden_trabajo_mecanico
    //    Devuelve filas: [idAreaLaboral, nombreArea, horasTotales, costoTotal]
    @Query(value = """
            SELECT al.id_area_laboral                       AS idAreaLaboral,
                   al.area                                  AS nombreArea,
                   COALESCE(SUM(otm.horas), 0)              AS horasTotales,
                   COALESCE(SUM(otm.costo_total), 0)        AS costoTotal
            FROM area_laboral al
            JOIN area_trabajos at
                 ON at.id_area_laboral = al.id_area_laboral
            JOIN mecanicos m
                 ON m.id_mecanico = at.id_mecanico
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_mecanico = m.id_mecanico
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY al.id_area_laboral, al.area
            ORDER BY horasTotales DESC
            """, nativeQuery = true)
    List<Object[]> horasYCostoPorAreaEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
