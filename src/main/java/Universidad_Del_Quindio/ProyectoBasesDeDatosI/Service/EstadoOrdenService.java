package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoOrden;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EstadoOrdenService {

    /**
     * Obtiene la lista completa de estados de orden registrados en el sistema.
     *
     * @return lista de {@link EstadoOrden}
     */
    List<EstadoOrden> listarTodos();

    /**
     * Busca un estado de orden por su identificador.
     *
     * @param idEstadoOrden identificador del estado (PK de la tabla estados_orden).
     * @return el {@link EstadoOrden} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    EstadoOrden obtenerPorId(Byte idEstadoOrden);

    /**
     * Crea y persiste un nuevo estado de orden en la base de datos.
     *
     * @param estadoOrden entidad {@link EstadoOrden} con la información a guardar.
     * @return el estado de orden guardado con su id generado.
     */
    EstadoOrden crear(EstadoOrden estadoOrden);

    /**
     * Actualiza los datos de un estado de orden existente.
     *
     * @param idEstadoOrden          identificador del estado a actualizar.
     * @param estadoOrdenActualizado objeto {@link EstadoOrden} con los nuevos datos
     *                               (se utiliza principalmente el campo estado).
     * @return el estado de orden actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    EstadoOrden actualizar(Byte idEstadoOrden, EstadoOrden estadoOrdenActualizado);

    /**
     * Elimina un estado de orden por su identificador.
     *
     * @param idEstadoOrden identificador del estado a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    void eliminar(Byte idEstadoOrden);

    // ===== Métodos que envuelven las consultas del EstadoOrdenRepository =====

    /**
     * Busca un estado de orden por su nombre exacto.
     *
     * @param estado nombre exacto del estado (columna estado).
     * @return el estado encontrado o null si no existe.
     */
    EstadoOrden obtenerPorNombre(String estado);

    /**
     * Busca estados de orden cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del estado.
     * @return lista de estados cuyo nombre contiene el texto dado.
     */
    List<EstadoOrden> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene los estados de orden creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de estados creados en ese rango.
     */
    List<EstadoOrden> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todos los estados de orden ordenados alfabéticamente por el campo estado.
     *
     * @return lista de estados ordenados ascendentemente por nombre.
     */
    List<EstadoOrden> listarOrdenadosPorNombre();

    /**
     * Cuenta cuántas órdenes de trabajo existen por cada estado.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoOrden (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → cantidadOrdenes (Long)</li>
     * </ul>
     *
     * @return lista de registros con la información [idEstadoOrden, nombreEstado, cantidadOrdenes].
     */
    List<Object[]> contarOrdenesPorEstado();

    /**
     * Obtiene los estados que no tienen ninguna orden de trabajo asociada.
     *
     * @return lista de estados sin órdenes.
     */
    List<EstadoOrden> listarEstadosSinOrdenes();

    /**
     * Calcula el promedio de días que duran las órdenes en cada estado,
     * considerando solo órdenes con fecha de salida NO nula.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoOrden (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → promedioDias (Double)</li>
     * </ul>
     *
     * @return lista de registros con [idEstadoOrden, nombreEstado, promedioDias].
     */
    List<Object[]> obtenerPromedioDiasPorEstado();

    /**
     * Calcula el promedio de días que duran las órdenes en cada estado,
     * filtrando por un rango de fechas de ingreso (fecha_ingreso entre inicio y fin).
     * Solo se consideran órdenes con fecha_salida NO nula.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoOrden (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → promedioDias (Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idEstadoOrden, nombreEstado, promedioDias].
     */
    List<Object[]> obtenerPromedioDiasPorEstadoEnRango(LocalDate inicio, LocalDate fin);
}
