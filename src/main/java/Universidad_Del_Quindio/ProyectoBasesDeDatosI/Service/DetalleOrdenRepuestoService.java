package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuestoId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DetalleOrdenRepuestoService {

    /**
     * Obtiene la lista completa de detalles de repuestos para órdenes de trabajo.
     *
     * @return lista de {@link DetalleOrdenRepuesto}
     */
    List<DetalleOrdenRepuesto> listarTodos();

    /**
     * Busca un detalle de repuesto por su ID compuesto
     * (id_orden_trabajo, id_repuesto).
     *
     * @param id objeto {@link DetalleOrdenRepuestoId} que contiene id_orden_trabajo e id_repuesto.
     * @return el {@link DetalleOrdenRepuesto} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un detalle con ese id.
     */
    DetalleOrdenRepuesto obtenerPorId(DetalleOrdenRepuestoId id);

    /**
     * Crea y persiste un nuevo detalle de repuesto para una orden de trabajo.
     * <p>
     * Nota: el {@link DetalleOrdenRepuestoId} se construye a partir de los IDs de
     * {@link DetalleOrdenRepuesto#getOrdenTrabajo()} y {@link DetalleOrdenRepuesto#getRepuesto()},
     * si aún es null.
     *
     * @param detalle entidad {@link DetalleOrdenRepuesto} con la información a guardar
     *                (ordenTrabajo, repuesto, cantidad, subTotal).
     * @return el detalle guardado.
     */
    DetalleOrdenRepuesto crear(DetalleOrdenRepuesto detalle);

    /**
     * Actualiza los datos de un detalle de repuesto existente.
     *
     * @param id                 identificador compuesto del detalle (orden + repuesto).
     * @param detalleActualizado objeto {@link DetalleOrdenRepuesto} con los nuevos datos
     *                           (cantidad, subTotal).
     * @return el detalle actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un detalle con ese id.
     */
    DetalleOrdenRepuesto actualizar(DetalleOrdenRepuestoId id, DetalleOrdenRepuesto detalleActualizado);

    /**
     * Elimina un detalle de repuesto por su ID compuesto.
     *
     * @param id identificador compuesto {@link DetalleOrdenRepuestoId} del detalle a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un detalle con ese id.
     */
    void eliminar(DetalleOrdenRepuestoId id);

    // ===== Métodos que envuelven las consultas del DetalleOrdenRepuestoRepository =====

    /**
     * Lista todos los detalles de repuestos asociados a una orden de trabajo específica.
     *
     * @param idOrdenTrabajo identificador de la orden de trabajo.
     * @return lista de detalles de repuestos de esa orden.
     */
    List<DetalleOrdenRepuesto> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo);

    /**
     * Lista todos los detalles de repuestos asociados a un repuesto específico.
     *
     * @param idRepuesto identificador del repuesto.
     * @return lista de detalles que usan ese repuesto.
     */
    List<DetalleOrdenRepuesto> listarPorIdRepuesto(Integer idRepuesto);

    /**
     * Obtiene los detalles cuyo campo cantidad es mayor o igual al valor indicado.
     *
     * @param cantidadMinima cantidad mínima (inclusive).
     * @return lista de detalles que cumplen con el criterio.
     */
    List<DetalleOrdenRepuesto> buscarPorCantidadMayorIgual(Short cantidadMinima);

    /**
     * Obtiene los detalles creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de detalles creados en ese rango.
     */
    List<DetalleOrdenRepuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Genera un resumen de repuestos por orden de trabajo, calculando
     * cantidad total y valor total de repuestos por orden.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idOrdenTrabajo (Integer o Number)</li>
     *     <li>[1] → cantidadTotal (Long o Number)</li>
     *     <li>[2] → totalRepuestos (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idOrdenTrabajo, cantidadTotal, totalRepuestos].
     */
    List<Object[]> obtenerResumenRepuestosPorOrden();

    /**
     * Obtiene los repuestos más utilizados (por cantidad total y valor total),
     * usando la tabla detalle_orden_repuesto.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → totalRepuestos (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idRepuesto, nombreRepuesto, cantidadTotal, totalRepuestos].
     */
    List<Object[]> obtenerRepuestosMasUtilizados();

    /**
     * Calcula el consumo de repuestos por cliente en un rango de fechas de ingreso
     * de la orden de trabajo (orden_trabajo.fecha_ingreso).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → totalRepuestos (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idCliente, nombreCliente, cantidadTotal, totalRepuestos].
     */
    List<Object[]> obtenerConsumoRepuestosPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin);
}
