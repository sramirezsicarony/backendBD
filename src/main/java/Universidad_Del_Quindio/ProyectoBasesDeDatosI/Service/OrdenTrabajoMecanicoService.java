package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanicoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdenTrabajoMecanicoService {

    /**
     * Obtiene la lista completa de registros de orden_trabajo_mecanico.
     *
     * @return lista de {@link OrdenTrabajoMecanico}
     */
    List<OrdenTrabajoMecanico> listarTodos();

    /**
     * Busca un registro de orden_trabajo_mecanico por su ID compuesto
     * (id_orden_trabajo, id_mecanico).
     *
     * @param id objeto {@link OrdenTrabajoMecanicoId} que contiene id_orden_trabajo e id_mecanico.
     * @return el registro encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    OrdenTrabajoMecanico obtenerPorId(OrdenTrabajoMecanicoId id);

    /**
     * Crea y persiste un nuevo registro de relación orden–mecánico–rol.
     * <p>
     * Nota: el {@link OrdenTrabajoMecanicoId} se construye a partir de los IDs de
     * {@link OrdenTrabajoMecanico#getOrdenTrabajo()} y {@link OrdenTrabajoMecanico#getMecanico()},
     * si aún es null.
     *
     * @param entidad entidad {@link OrdenTrabajoMecanico} con la información a guardar
     *                (ordenTrabajo, mecanico, rol, horas, costoHora, costoTotal).
     * @return el registro guardado.
     */
    OrdenTrabajoMecanico crear(OrdenTrabajoMecanico entidad);

    /**
     * Actualiza los datos de un registro existente de orden_trabajo_mecanico.
     *
     * @param id                 identificador compuesto del registro (orden + mecánico).
     * @param entidadActualizada objeto {@link OrdenTrabajoMecanico} con los nuevos datos
     *                           (rol, horas, costoHora, costoTotal).
     * @return el registro actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    OrdenTrabajoMecanico actualizar(OrdenTrabajoMecanicoId id, OrdenTrabajoMecanico entidadActualizada);

    /**
     * Elimina un registro de orden_trabajo_mecanico por su identificador compuesto.
     *
     * @param id identificador compuesto {@link OrdenTrabajoMecanicoId} del registro a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    void eliminar(OrdenTrabajoMecanicoId id);

    // ===== Métodos que envuelven las consultas del Repository =====

    /**
     * Lista todos los registros asociados a una orden de trabajo específica.
     *
     * @param idOrdenTrabajo identificador de la orden de trabajo.
     * @return lista de registros de esa orden.
     */
    List<OrdenTrabajoMecanico> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo);

    /**
     * Lista todos los registros asociados a un mecánico específico.
     *
     * @param idMecanico identificador del mecánico (DNI).
     * @return lista de registros en los que participa ese mecánico.
     */
    List<OrdenTrabajoMecanico> listarPorIdMecanico(String idMecanico);

    /**
     * Lista todos los registros cuyo rol en la orden corresponde a un rol específico.
     *
     * @param idRol identificador del rol.
     * @return lista de registros con ese rol.
     */
    List<OrdenTrabajoMecanico> listarPorIdRol(Byte idRol);

    /**
     * Obtiene los registros donde las horas trabajadas están dentro de un rango.
     *
     * @param horasMin horas mínimas (inclusive).
     * @param horasMax horas máximas (inclusive).
     * @return lista de registros que cumplen el criterio.
     */
    List<OrdenTrabajoMecanico> buscarPorRangoHoras(BigDecimal horasMin, BigDecimal horasMax);

    /**
     * Obtiene los registros creados dentro de un rango de fechas (created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de registros creados en ese rango.
     */
    List<OrdenTrabajoMecanico> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula horas totales y costo total de mano de obra por mecánico
     * en un rango de fechas (según created_at).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idMecanico (String)</li>
     *     <li>[1] → horasTotales (BigDecimal/Double/Number)</li>
     *     <li>[2] → costoTotal (BigDecimal/Double/Number)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idMecanico, horasTotales, costoTotal].
     */
    List<Object[]> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula horas totales y costo total de mano de obra por orden de trabajo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idOrdenTrabajo (Integer/Number)</li>
     *     <li>[1] → horasTotales (BigDecimal/Double/Number)</li>
     *     <li>[2] → costoTotal (BigDecimal/Double/Number)</li>
     * </ul>
     *
     * @return lista de registros con [idOrdenTrabajo, horasTotales, costoTotal].
     */
    List<Object[]> obtenerHorasYCostoTotalPorOrden();

    /**
     * Calcula horas totales y costo total de mano de obra por rol de mecánico
     * en un rango de fechas (según created_at).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRol (Byte/Number)</li>
     *     <li>[1] → nombreRol (String)</li>
     *     <li>[2] → horasTotales (BigDecimal/Double/Number)</li>
     *     <li>[3] → costoTotal (BigDecimal/Double/Number)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idRol, nombreRol, horasTotales, costoTotal].
     */
    List<Object[]> obtenerHorasYCostoTotalPorRolEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
