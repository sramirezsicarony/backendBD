package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Byte> {


    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<Rol> findById(Byte idRol);

    // 2) Listar todos los roles
    @Override
    List<Rol> findAll();

    // 3) Buscar un rol por su nombre exacto
    Rol findByRol(String rol);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar roles cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Rol> findByRolContainingIgnoreCase(String texto);

    // 5) Buscar roles creados en un rango de fechas
    List<Rol> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar roles ordenados alfabéticamente por nombre
    List<Rol> findAllByOrderByRolAsc();


    // ================== CONSULTAS COMPLEJAS ==================
    // Usan la tabla REAL: orden_trabajo_mecanico (id_rol FK a roles)

    // 7) Contar cuántas asignaciones (orden_trabajo_mecanico) tiene cada rol
    //    Devuelve filas: [idRol, nombreRol, cantidadAsignaciones]
    @Query(value = """
            SELECT r.id_rol                         AS idRol,
                   r.rol                            AS nombreRol,
                   COUNT(otm.id_mecanico)           AS cantidadAsignaciones
            FROM roles r
            LEFT JOIN orden_trabajo_mecanico otm
                   ON otm.id_rol = r.id_rol
            GROUP BY r.id_rol, r.rol
            ORDER BY cantidadAsignaciones DESC
            """, nativeQuery = true)
    List<Object[]> contarAsignacionesPorRol();

    // 8) Roles que NO tienen ninguna asignación en orden_trabajo_mecanico
    @Query(value = """
            SELECT r.*
            FROM roles r
            LEFT JOIN orden_trabajo_mecanico otm
                   ON otm.id_rol = r.id_rol
            WHERE otm.id_orden_trabajo IS NULL
            """, nativeQuery = true)
    List<Rol> findRolesSinAsignaciones();

    // 9) Horas totales por rol en un rango de fechas (según created_at de orden_trabajo_mecanico)
    //    Devuelve filas: [idRol, nombreRol, horasTotales]
    @Query(value = """
            SELECT r.id_rol                         AS idRol,
                   r.rol                            AS nombreRol,
                   COALESCE(SUM(otm.horas), 0)      AS horasTotales
            FROM roles r
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_rol = r.id_rol
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY r.id_rol, r.rol
            ORDER BY horasTotales DESC
            """, nativeQuery = true)
    List<Object[]> horasTotalesPorRolEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
