package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaLaboral;

import java.time.LocalDateTime;
import java.util.List;

public interface AreaLaboralService {

    /**
     * Obtiene la lista completa de áreas laborales registradas en el sistema.
     *
     * @return lista de {@link AreaLaboral}
     */
    List<AreaLaboral> listarTodas();

    /**
     * Busca un área laboral por su identificador.
     *
     * @param idAreaLaboral identificador del área laboral
     *                      (PK de la tabla area_laboral).
     * @return el {@link AreaLaboral} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un área con ese id.
     */
    AreaLaboral obtenerPorId(Integer idAreaLaboral);

    /**
     * Crea y persiste una nueva área laboral en la base de datos.
     *
     * @param areaLaboral entidad {@link AreaLaboral} con la información a guardar.
     * @return el área laboral guardada con su id generado.
     */
    AreaLaboral crear(AreaLaboral areaLaboral);

    /**
     * Actualiza los datos de un área laboral existente.
     *
     * @param idAreaLaboral          identificador del área a actualizar.
     * @param areaLaboralActualizada objeto {@link AreaLaboral} con los nuevos datos
     *                               (principalmente el campo area).
     * @return el área laboral actualizada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un área con ese id.
     */
    AreaLaboral actualizar(Integer idAreaLaboral, AreaLaboral areaLaboralActualizada);

    /**
     * Elimina un área laboral por su identificador.
     *
     * @param idAreaLaboral identificador del área laboral a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un área con ese id.
     */
    void eliminar(Integer idAreaLaboral);

    // ===== Métodos que envuelven las consultas del AreaLaboralRepository =====

    /**
     * Busca un área laboral por su nombre exacto.
     *
     * @param nombreArea nombre exacto del área (columna area).
     * @return el área laboral encontrada o null si no existe.
     */
    AreaLaboral obtenerPorNombre(String nombreArea);

    /**
     * Busca áreas laborales cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del área.
     * @return lista de áreas laborales cuyo nombre contiene el texto dado.
     */
    List<AreaLaboral> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene las áreas laborales creadas dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de áreas laborales creadas en ese rango.
     */
    List<AreaLaboral> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todas las áreas laborales ordenadas alfabéticamente por el campo area.
     *
     * @return lista de áreas ordenadas ascendentemente por nombre.
     */
    List<AreaLaboral> listarOrdenadasPorNombre();

    /**
     * Cuenta cuántos mecánicos hay en cada área laboral, utilizando la tabla area_trabajos.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAreaLaboral (Integer o Number)</li>
     *     <li>[1] → nombreArea (String)</li>
     *     <li>[2] → cantidadMecanicos (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idAreaLaboral, nombreArea, cantidadMecanicos].
     */
    List<Object[]> contarMecanicosPorAreaLaboral();

    /**
     * Obtiene las áreas laborales que no tienen ningún mecánico asociado
     * (no tienen registros en la tabla area_trabajos).
     *
     * @return lista de áreas sin mecánicos.
     */
    List<AreaLaboral> listarAreasSinMecanicos();

    /**
     * Calcula las horas totales trabajadas por área laboral en un rango de fechas,
     * usando las tablas area_trabajos, mecanicos y orden_trabajo_mecanico.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAreaLaboral (Integer o Number)</li>
     *     <li>[1] → nombreArea (String)</li>
     *     <li>[2] → horasTotales (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idAreaLaboral, nombreArea, horasTotales].
     */
    List<Object[]> obtenerHorasTotalesPorAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
