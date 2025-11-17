package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajos;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajosId;

import java.time.LocalDateTime;
import java.util.List;

public interface AreaTrabajosService {

    /**
     * Obtiene la lista completa de asignaciones área–mecánico.
     *
     * @return lista de {@link AreaTrabajos}
     */
    List<AreaTrabajos> listarTodos();

    /**
     * Busca una asignación área–mecánico por su ID compuesto
     * (id_area_laboral, id_mecanico).
     *
     * @param id objeto {@link AreaTrabajosId} que contiene id_area_laboral e id_mecanico.
     * @return la asignación encontrada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    AreaTrabajos obtenerPorId(AreaTrabajosId id);

    /**
     * Crea y persiste una nueva asignación área–mecánico.
     * <p>
     * Nota: el {@link AreaTrabajosId} se construye a partir de los IDs de
     * {@link AreaTrabajos#getAreaLaboral()} y {@link AreaTrabajos#getMecanico()},
     * si aún es null.
     *
     * @param entidad entidad {@link AreaTrabajos} con la información a guardar
     *                (areaLaboral, mecanico).
     * @return la asignación guardada.
     */
    AreaTrabajos crear(AreaTrabajos entidad);

    /**
     * Actualiza una asignación área–mecánico existente.
     * <p>
     * Por tratarse de una relación con clave compuesta (área + mecánico),
     * normalmente NO se recomienda cambiar las claves. Si necesitas cambiar
     * el mecánico o el área, es mejor eliminar el registro y crear uno nuevo.
     *
     * @param id               identificador compuesto (área + mecánico).
     * @param entidadActualizada objeto {@link AreaTrabajos} con los nuevos datos
     *                           (por ahora la entidad solo maneja relaciones).
     * @return la asignación (en la práctica, suele mantenerse igual).
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    AreaTrabajos actualizar(AreaTrabajosId id, AreaTrabajos entidadActualizada);

    /**
     * Elimina una asignación área–mecánico por su identificador compuesto.
     *
     * @param id identificador compuesto {@link AreaTrabajosId} del registro a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    void eliminar(AreaTrabajosId id);

    // ===== Métodos que envuelven las consultas del Repository =====

    /**
     * Lista todas las asignaciones de una área laboral específica.
     *
     * @param idAreaLaboral identificador del área laboral.
     * @return lista de asignaciones para esa área.
     */
    List<AreaTrabajos> listarPorIdAreaLaboral(Integer idAreaLaboral);

    /**
     * Lista todas las asignaciones en las que participa un mecánico específico.
     *
     * @param idMecanico identificador del mecánico (DNI).
     * @return lista de asignaciones de ese mecánico.
     */
    List<AreaTrabajos> listarPorIdMecanico(String idMecanico);

    /**
     * Obtiene las asignaciones creadas dentro de un rango de fechas (created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de asignaciones creadas en ese rango.
     */
    List<AreaTrabajos> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista asignaciones cuyo nombre de área (tabla area_laboral)
     * contenga cierto texto (LIKE, ignorando mayúsculas).
     *
     * @param texto parte del nombre del área a buscar.
     * @return lista de asignaciones correspondientes.
     */
    List<AreaTrabajos> buscarPorNombreAreaConteniendo(String texto);

    /**
     * Cuenta cuántos mecánicos tiene asignados cada área laboral.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAreaLaboral (Integer/Number)</li>
     *     <li>[1] → nombreArea (String)</li>
     *     <li>[2] → cantidadMecanicos (Long/Number)</li>
     * </ul>
     *
     * @return lista de registros con [idAreaLaboral, nombreArea, cantidadMecanicos].
     */
    List<Object[]> contarMecanicosPorAreaLaboral();

    /**
     * Cuenta en cuántas áreas laborales está asignado cada mecánico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idMecanico (String)</li>
     *     <li>[1] → cantidadAreas (Long/Number)</li>
     * </ul>
     *
     * @return lista de registros con [idMecanico, cantidadAreas].
     */
    List<Object[]> contarAreasPorMecanico();

    /**
     * Calcula horas totales y costo total de mano de obra por área laboral
     * en un rango de fechas (según created_at de orden_trabajo_mecanico).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAreaLaboral (Integer/Number)</li>
     *     <li>[1] → nombreArea (String)</li>
     *     <li>[2] → horasTotales (Number, BigDecimal/Double)</li>
     *     <li>[3] → costoTotal (Number, BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idAreaLaboral, nombreArea, horasTotales, costoTotal].
     */
    List<Object[]> obtenerHorasYCostoPorAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
