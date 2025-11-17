package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanicoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoMecanicoRepository extends JpaRepository<OrdenTrabajoMecanico, OrdenTrabajoMecanicoId> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar registro por ID compuesto (id_orden_trabajo, id_mecanico)
    @Override
    Optional<OrdenTrabajoMecanico> findById(OrdenTrabajoMecanicoId id);

    // 2) Listar todos los registros orden_trabajo_mecanico
    @Override
    List<OrdenTrabajoMecanico> findAll();

    // 3) Listar todos los registros de una orden específica
    List<OrdenTrabajoMecanico> findByOrdenTrabajo_IdOrdenTrabajo(Integer idOrdenTrabajo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar todos los registros de un mecánico específico
    List<OrdenTrabajoMecanico> findByMecanico_IdMecanico(String idMecanico);

    // 5) Listar registros cuyo rol en la orden sea un rol específico
    List<OrdenTrabajoMecanico> findByRol_IdRol(Byte idRol);

    // 6) Registros donde las horas trabajadas estén en un rango
    List<OrdenTrabajoMecanico> findByHorasBetween(BigDecimal horasMin, BigDecimal horasMax);


    // ================== CONSULTAS INTERMEDIAS (extra útiles por fecha) ==================

    // 7) Registros creados en un rango de fechas
    List<OrdenTrabajoMecanico> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales:
    //  - orden_trabajo_mecanico (esta)
    //  - mecanicos (id_mecanico, experiencia, id_especialidad, costo_hora)
    //  - roles (id_rol, rol)
    //  - orden_trabajo (id_orden_trabajo, fecha_ingreso, fecha_salida)
    //  - vehiculos, clientes, etc. (podrían usarse en otros reportes)

    // 1) Horas totales y costo total por mecánico en un rango de fechas (created_at)
    //    Devuelve filas: [idMecanico, horasTotales, costoTotal]
    @Query(value = """
            SELECT otm.id_mecanico                      AS idMecanico,
                   COALESCE(SUM(otm.horas), 0)          AS horasTotales,
                   COALESCE(SUM(otm.costo_total), 0)    AS costoTotal
            FROM orden_trabajo_mecanico otm
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY otm.id_mecanico
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> horasYCostoTotalPorMecanicoEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // 2) Horas totales y costo total por orden de trabajo
    //    Devuelve filas: [idOrdenTrabajo, horasTotales, costoTotal]
    @Query(value = """
            SELECT otm.id_orden_trabajo                 AS idOrdenTrabajo,
                   COALESCE(SUM(otm.horas), 0)          AS horasTotales,
                   COALESCE(SUM(otm.costo_total), 0)    AS costoTotal
            FROM orden_trabajo_mecanico otm
            GROUP BY otm.id_orden_trabajo
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> horasYCostoTotalPorOrden();

    // 3) Horas totales y costo total por rol de mecánico en un rango de fechas
    //    Devuelve filas: [idRol, nombreRol, horasTotales, costoTotal]
    @Query(value = """
            SELECT r.id_rol                             AS idRol,
                   r.rol                                AS nombreRol,
                   COALESCE(SUM(otm.horas), 0)          AS horasTotales,
                   COALESCE(SUM(otm.costo_total), 0)    AS costoTotal
            FROM roles r
            JOIN orden_trabajo_mecanico otm
                 ON otm.id_rol = r.id_rol
            WHERE otm.created_at BETWEEN :inicio AND :fin
            GROUP BY r.id_rol, r.rol
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> horasYCostoTotalPorRolEnRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
