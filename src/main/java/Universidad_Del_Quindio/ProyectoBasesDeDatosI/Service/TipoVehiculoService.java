package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.TipoVehiculo;

import java.time.LocalDateTime;
import java.util.List;

public interface TipoVehiculoService {

    /**
     * Obtiene la lista completa de tipos de vehículo.
     *
     * @return lista de {@link TipoVehiculo}
     */
    List<TipoVehiculo> listarTodos();

    /**
     * Busca un tipo de vehículo por su identificador.
     *
     * @param idTipoVehiculo identificador del tipo de vehículo (PK).
     * @return el tipo de vehículo encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un tipo con ese id.
     */
    TipoVehiculo obtenerPorId(Integer idTipoVehiculo);

    /**
     * Crea y persiste un nuevo tipo de vehículo en la base de datos.
     *
     * @param tipoVehiculo entidad {@link TipoVehiculo} con la información a guardar.
     * @return el tipo de vehículo guardado con su id generado.
     */
    TipoVehiculo crear(TipoVehiculo tipoVehiculo);

    /**
     * Actualiza los datos de un tipo de vehículo existente.
     *
     * @param idTipoVehiculo     identificador del tipo de vehículo a actualizar.
     * @param tipoActualizado    objeto {@link TipoVehiculo} con los nuevos datos (solo campo tipo).
     * @return el tipo de vehículo actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un tipo con ese id.
     */
    TipoVehiculo actualizar(Integer idTipoVehiculo, TipoVehiculo tipoActualizado);

    /**
     * Elimina un tipo de vehículo por su identificador.
     *
     * @param idTipoVehiculo identificador del tipo a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un tipo con ese id.
     */
    void eliminar(Integer idTipoVehiculo);

    // ===== Métodos que envuelven las consultas del Repository =====

    /**
     * Busca un tipo de vehículo por su nombre exacto.
     *
     * @param tipo nombre exacto del tipo de vehículo.
     * @return la entidad {@link TipoVehiculo} encontrada o null si no existe.
     */
    TipoVehiculo buscarPorTipoExacto(String tipo);

    /**
     * Busca tipos de vehículo cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas).
     *
     * @param texto fragmento del nombre a buscar.
     * @return lista de tipos de vehículo que cumplen el criterio.
     */
    List<TipoVehiculo> buscarPorTipoConteniendo(String texto);

    /**
     * Obtiene los tipos de vehículo creados en un rango de fechas (created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de tipos de vehículo creados en ese rango.
     */
    List<TipoVehiculo> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todos los tipos de vehículo ordenados alfabéticamente por nombre.
     *
     * @return lista de {@link TipoVehiculo} ordenada por el campo tipo.
     */
    List<TipoVehiculo> listarOrdenadosPorNombre();

    /**
     * Cuenta cuántos vehículos hay por cada tipo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idTipoVehiculo (Integer/Number)</li>
     *     <li>[1] → nombreTipo (String)</li>
     *     <li>[2] → cantidadVehiculos (Long/Number)</li>
     * </ul>
     *
     * @return lista de filas con [idTipoVehiculo, nombreTipo, cantidadVehiculos].
     */
    List<Object[]> contarVehiculosPorTipo();

    /**
     * Obtiene los tipos de vehículo que no tienen ningún vehículo asociado.
     *
     * @return lista de {@link TipoVehiculo} sin vehículos registrados.
     */
    List<TipoVehiculo> buscarTiposSinVehiculos();

    /**
     * Cuenta cuántos vehículos tiene cada tipo dentro de un rango de años de modelo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idTipoVehiculo (Integer/Number)</li>
     *     <li>[1] → nombreTipo (String)</li>
     *     <li>[2] → cantidadVehiculos (Long/Number)</li>
     * </ul>
     *
     * @param anioInicio año mínimo del modelo (inclusive).
     * @param anioFin    año máximo del modelo (inclusive).
     * @return lista de filas con [idTipoVehiculo, nombreTipo, cantidadVehiculos].
     */
    List<Object[]> contarVehiculosPorTipoEnRangoAnio(int anioInicio, int anioFin);
}
