package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AreaLaboralRepository extends JpaRepository<AreaLaboral, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<AreaLaboral> findById(Integer idAreaLaboral);

    // 2) Listar todas las áreas laborales
    @Override
    List<AreaLaboral> findAll();

    // 3) Buscar un área laboral por su nombre exacto
    AreaLaboral findByArea(String area);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar áreas cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<AreaLaboral> findByAreaContainingIgnoreCase(String texto);

    // 5) Buscar áreas creadas en un rango de fechas
    List<AreaLaboral> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar áreas ordenadas alfabéticamente por nombre
    List<AreaLaboral> findAllByOrderByAreaAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - area_trabajos (id_area_laboral FK -> area_laboral, id_mecanico FK -> mecanicos)
    //  - mecanicos (id_mecanico PK)
    //  - orden_trabajo_mecanico (id_mecanico FK -> mecanicos, horas, costo_total, created_at)

    // 7) Contar cuántos mecánicos hay en cada área laboral
    //    Devuelve filas: [idAreaLaboral, nombreArea, cantidadMecanicos]
    @Query(value = """
            SELECT al.id_area_laboral                     AS idAreaLaboral,
                   al.area                                AS nombreArea,
                   COALESCE(COUNT(DISTINCT at.id_mecanico), 0) AS cantidadMecanicos
            FROM area_laboral al
            LEFT JOIN area_trabajos at
                   ON at.id_area_laboral = al.id_area_laboral
            GROUP BY al.id_area_laboral, al.area
            ORDER BY cantidadMecanicos DESC
            """, nativeQuery = true)
    List<Object[]> contarMecanicosPorAreaLaboral();

    // 8) Áreas laborales que NO tienen ningún mecánico asociado
    @Query(value = """
            SELECT al.*
            FROM area_laboral al
            LEFT JOIN area_trabajos at
                   ON at.id_area_laboral = al.id_area_laboral
            WHERE at.id_mecanico IS NULL
            """, nativeQuery = true)
    List<AreaLaboral> findAreasSinMecanicos();

    // 9) Horas totales trabajadas por área laboral en un rango de fechas
    //    (según created_at de orden_trabajo_mecanico)
    //    Devuelve filas: [idAreaLaboral, nombreArea, horasTotales]
    @Query(value = """
            SELECT al.id_area_laboral                     AS idAreaLaboral,
                   al.area                                AS nombreArea,
                   COALESCE(SUM(otm.horas), 0)            AS horasTotales
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
    List<Object[]> horasTotalesPorAreaEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
