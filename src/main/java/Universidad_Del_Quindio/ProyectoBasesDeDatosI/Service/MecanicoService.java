package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Mecanico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MecanicoService {

    /**
     * Obtiene la lista completa de mecánicos registrados en el sistema.
     *
     * @return lista de {@link Mecanico}
     */
    List<Mecanico> listarTodos();

    /**
     * Busca un mecánico por su identificador (DNI).
     *
     * @param idMecanico identificador del mecánico (PK de la tabla mecanicos).
     * @return el {@link Mecanico} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un mecánico con ese id.
     */
    Mecanico obtenerPorId(String idMecanico);

    /**
     * Crea y persiste un nuevo mecánico en la base de datos.
     * <p>
     * Nota: el idMecanico no es autogenerado, debe venir informado en la entidad.
     *
     * @param mecanico entidad {@link Mecanico} con la información a guardar.
     * @return el mecánico guardado.
     */
    Mecanico crear(Mecanico mecanico);

    /**
     * Actualiza los datos de un mecánico existente.
     *
     * @param idMecanico        identificador del mecánico a actualizar.
     * @param mecanicoActualizado objeto {@link Mecanico} con los nuevos datos
     *                            (se usan experiencia, especialidad y costoHora).
     * @return el mecánico actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un mecánico con ese id.
     */
    Mecanico actualizar(String idMecanico, Mecanico mecanicoActualizado);

    /**
     * Elimina un mecánico por su identificador.
     *
     * @param idMecanico identificador del mecánico a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un mecánico con ese id.
     */
    void eliminar(String idMecanico);

    // ===== Métodos que envuelven las consultas del MecanicoRepository =====

    /**
     * Lista los mecánicos que pertenecen a una especialidad específica.
     *
     * @param idEspecialidad identificador de la especialidad.
     * @return lista de mecánicos asociados a esa especialidad.
     */
    List<Mecanico> listarPorIdEspecialidad(Integer idEspecialidad);

    /**
     * Busca mecánicos cuya experiencia (años) sea mayor o igual al valor indicado.
     *
     * @param experiencia valor mínimo de experiencia.
     * @return lista de mecánicos que cumplen con el criterio.
     */
    List<Mecanico> buscarPorExperienciaMayorIgual(Byte experiencia);

    /**
     * Busca mecánicos cuyo costo por hora se encuentre dentro de un rango.
     *
     * @param costoMin costo mínimo (inclusive).
     * @param costoMax costo máximo (inclusive).
     * @return lista de mecánicos cuyo costo_hora está en ese rango.
     */
    List<Mecanico> buscarPorRangoCostoHora(BigDecimal costoMin, BigDecimal costoMax);

    /**
     * Obtiene los mecánicos creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de mecánicos creados en ese rango.
     */
    List<Mecanico> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula las horas totales trabajadas y el costo total de mano de obra
     * por mecánico en un rango de fechas, según la tabla orden_trabajo_mecanico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idMecanico (String)</li>
     *     <li>[1] → experiencia (Byte o Number)</li>
     *     <li>[2] → horasTotales (BigDecimal/Double/Number)</li>
     *     <li>[3] → costoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idMecanico, experiencia, horasTotales, costoTotal].
     */
    List<Object[]> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene los mecánicos que nunca han sido asignados a una orden de trabajo
     * (no tienen registros en orden_trabajo_mecanico).
     *
     * @return lista de mecánicos sin órdenes asignadas.
     */
    List<Mecanico> listarMecanicosSinOrdenes();

    /**
     * Calcula las horas totales trabajadas por mecánico y área laboral en un rango de fechas,
     * usando las tablas area_trabajos, area_laboral y orden_trabajo_mecanico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idMecanico (String)</li>
     *     <li>[1] → idAreaLaboral (Integer o Number)</li>
     *     <li>[2] → nombreArea (String)</li>
     *     <li>[3] → horasTotales (BigDecimal/Double/Number)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idMecanico, idAreaLaboral, nombreArea, horasTotales].
     */
    List<Object[]> obtenerHorasPorMecanicoYAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
