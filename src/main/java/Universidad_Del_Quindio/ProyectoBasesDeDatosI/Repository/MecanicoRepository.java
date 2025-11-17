package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, String> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar mecánico por id (DNI)
    @Override
    Optional<Mecanico> findById(String idMecanico);

    // 2) Listar todos los mecánicos
    @Override
    List<Mecanico> findAll();

    // 3) Buscar mecánicos por id de especialidad
    List<Mecanico> findByEspecialidad_IdEspecialidad(Integer idEspecialidad);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Mecánicos con experiencia mayor o igual a un valor
    List<Mecanico> findByExperienciaGreaterThanEqual(Byte experiencia);

    // 5) Mecánicos cuyo costo_hora está en un rango
    List<Mecanico> findByCostoHoraBetween(BigDecimal costoMin, BigDecimal costoMax);

    // 6) Mecánicos creados en un rango de fechas
    List<Mecanico> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - orden_trabajo_mecanico (id_mecanico FK -> mecanicos, horas, costo_total, created_at)
    //  - area_trabajos (id_mecanico FK -> mecanicos, id_area_laboral)
    //  - area_laboral (id_area_laboral PK)
    //  - especialidades (id_especialidad PK, join por relacion en la entidad)

    // 7) Horas totales y costo total de mano de obra por mecánico en un rango de fechas
    //    (según created_at de orden_trabajo_mecanico)
    //    Devuelve filas: [idMecanico, experiencia, horasTotales, costoTotal]
    @Query(value = """
            SELECT m.id_mecanico                              AS idMecanico,
                   m.experiencia                              AS experiencia,
                   COALESCE(SUM(otm.horas), 0)                AS horasTotales,
                   COALESCE(SUM(otm.costo_total), 0)          AS costoTotal
            FROM mecanicos m
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_mecanico = m.id_mecanico
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY m.id_mecanico, m.experiencia
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> horasYCostoTotalPorMecanicoEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // 8) Mecánicos que nunca han sido asignados a una orden de trabajo
    @Query(value = """
            SELECT m.*
            FROM mecanicos m
            LEFT JOIN orden_trabajo_mecanico otm
                   ON otm.id_mecanico = m.id_mecanico
            WHERE otm.id_orden_trabajo IS NULL
            """, nativeQuery = true)
    List<Mecanico> findMecanicosSinOrdenes();

    // 9) Horas totales trabajadas por mecánico y área laboral en un rango de fechas
    //    (usa area_trabajos + orden_trabajo_mecanico)
    //    Devuelve filas: [idMecanico, idAreaLaboral, nombreArea, horasTotales]
    @Query(value = """
            SELECT m.id_mecanico                              AS idMecanico,
                   al.id_area_laboral                         AS idAreaLaboral,
                   al.area                                    AS nombreArea,
                   COALESCE(SUM(otm.horas), 0)                AS horasTotales
            FROM mecanicos m
            JOIN area_trabajos at
                 ON at.id_mecanico = m.id_mecanico
            JOIN area_laboral al
                 ON al.id_area_laboral = at.id_area_laboral
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_mecanico = m.id_mecanico
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY m.id_mecanico, al.id_area_laboral, al.area
            ORDER BY horasTotales DESC
            """, nativeQuery = true)
    List<Object[]> horasPorMecanicoYAreaEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
