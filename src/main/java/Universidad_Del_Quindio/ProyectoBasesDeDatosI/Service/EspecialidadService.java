package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Especialidad;

import java.time.LocalDateTime;
import java.util.List;

public interface EspecialidadService {

    /**
     * Obtiene la lista completa de especialidades registradas en el sistema.
     *
     * @return lista de {@link Especialidad}
     */
    List<Especialidad> listarTodas();

    /**
     * Busca una especialidad por su identificador.
     *
     * @param idEspecialidad identificador de la especialidad (PK de la tabla especialidades).
     * @return la {@link Especialidad} encontrada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una especialidad con ese id.
     */
    Especialidad obtenerPorId(Integer idEspecialidad);

    /**
     * Crea y persiste una nueva especialidad en la base de datos.
     *
     * @param especialidad entidad {@link Especialidad} con la información a guardar.
     * @return la especialidad guardada con su id generado.
     */
    Especialidad crear(Especialidad especialidad);

    /**
     * Actualiza los datos de una especialidad existente.
     *
     * @param idEspecialidad       identificador de la especialidad a actualizar.
     * @param especialidadActualizada objeto {@link Especialidad} con los nuevos datos
     *                                (se usan nombreEspecialidad y descripcion).
     * @return la especialidad actualizada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una especialidad con ese id.
     */
    Especialidad actualizar(Integer idEspecialidad, Especialidad especialidadActualizada);

    /**
     * Elimina una especialidad por su identificador.
     *
     * @param idEspecialidad identificador de la especialidad a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una especialidad con ese id.
     */
    void eliminar(Integer idEspecialidad);

    // ===== Métodos que envuelven las consultas del EspecialidadRepository =====

    /**
     * Busca una especialidad por su nombre exacto.
     *
     * @param nombreEspecialidad nombre exacto de la especialidad (columna nombre_especialidad).
     * @return la especialidad encontrada o null si no existe.
     */
    Especialidad obtenerPorNombre(String nombreEspecialidad);

    /**
     * Busca especialidades cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre de la especialidad.
     * @return lista de especialidades cuyo nombre contiene el texto dado.
     */
    List<Especialidad> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene las especialidades creadas dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de especialidades creadas en ese rango.
     */
    List<Especialidad> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todas las especialidades ordenadas alfabéticamente por el campo nombreEspecialidad.
     *
     * @return lista de especialidades ordenadas ascendentemente por nombre.
     */
    List<Especialidad> listarOrdenadasPorNombre();

    /**
     * Cuenta cuántos mecánicos existen por cada especialidad.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEspecialidad (Integer o Number)</li>
     *     <li>[1] → nombreEspecialidad (String)</li>
     *     <li>[2] → cantidadMecanicos (Long)</li>
     * </ul>
     *
     * @return lista de registros con la información [idEspecialidad, nombreEspecialidad, cantidadMecanicos].
     */
    List<Object[]> contarMecanicosPorEspecialidad();

    /**
     * Obtiene las especialidades que no tienen ningún mecánico asociado.
     *
     * @return lista de especialidades sin mecánicos.
     */
    List<Especialidad> listarEspecialidadesSinMecanicos();

    /**
     * Calcula el costo total de mano de obra por especialidad en un rango de fechas,
     * tomando como referencia el campo created_at de orden_trabajo_mecanico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEspecialidad (Integer o Number)</li>
     *     <li>[1] → nombreEspecialidad (String)</li>
     *     <li>[2] → costoTotalManoObra (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idEspecialidad, nombreEspecialidad, costoTotalManoObra].
     */
    List<Object[]> obtenerCostoTotalManoObraPorEspecialidadEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
