package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoFactura;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EstadoFacturaService {

    /**
     * Obtiene la lista completa de estados de factura registrados en el sistema.
     *
     * @return lista de {@link EstadoFactura}
     */
    List<EstadoFactura> listarTodos();

    /**
     * Busca un estado de factura por su identificador.
     *
     * @param idEstadoFactura identificador del estado (PK de la tabla estado_factura).
     * @return el {@link EstadoFactura} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    EstadoFactura obtenerPorId(Byte idEstadoFactura);

    /**
     * Crea y persiste un nuevo estado de factura en la base de datos.
     *
     * @param estadoFactura entidad {@link EstadoFactura} con la información a guardar.
     * @return el estado de factura guardado con su id generado.
     */
    EstadoFactura crear(EstadoFactura estadoFactura);

    /**
     * Actualiza los datos de un estado de factura existente.
     *
     * @param idEstadoFactura          identificador del estado a actualizar.
     * @param estadoFacturaActualizado objeto {@link EstadoFactura} con los nuevos datos
     *                                 (se utiliza principalmente el campo estado).
     * @return el estado de factura actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    EstadoFactura actualizar(Byte idEstadoFactura, EstadoFactura estadoFacturaActualizado);

    /**
     * Elimina un estado de factura por su identificador.
     *
     * @param idEstadoFactura identificador del estado a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un estado con ese id.
     */
    void eliminar(Byte idEstadoFactura);

    // ===== Métodos que envuelven las consultas del EstadoFacturaRepository =====

    /**
     * Busca un estado de factura por su nombre exacto.
     *
     * @param estado nombre exacto del estado (columna estado).
     * @return el estado encontrado o null si no existe.
     */
    EstadoFactura obtenerPorNombre(String estado);

    /**
     * Busca estados de factura cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del estado.
     * @return lista de estados cuyo nombre contiene el texto dado.
     */
    List<EstadoFactura> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene los estados de factura creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de estados creados en ese rango.
     */
    List<EstadoFactura> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todos los estados de factura ordenados alfabéticamente por el campo estado.
     *
     * @return lista de estados ordenados ascendentemente por nombre.
     */
    List<EstadoFactura> listarOrdenadosPorNombre();

    /**
     * Cuenta cuántas facturas existen por cada estado.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoFactura (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → cantidadFacturas (Long)</li>
     * </ul>
     *
     * @return lista de registros con la información [idEstadoFactura, nombreEstado, cantidadFacturas].
     */
    List<Object[]> contarFacturasPorEstado();

    /**
     * Obtiene los estados de factura que no tienen ninguna factura asociada.
     *
     * @return lista de estados sin facturas.
     */
    List<EstadoFactura> listarEstadosSinFacturas();

    /**
     * Calcula el total facturado (SUM(total)) por estado en un rango de fechas de creación
     * (usa columna fecha_creacion de facturas).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoFactura (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → totalFacturado (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idEstadoFactura, nombreEstado, totalFacturado].
     */
    List<Object[]> obtenerTotalFacturadoPorEstadoEnRangoFechas(LocalDate inicio, LocalDate fin);
}
