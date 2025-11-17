package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajo;

import java.time.LocalDate;
import java.util.List;

public interface OrdenTrabajoService {

    /**
     * Obtiene la lista completa de órdenes de trabajo registradas en el sistema.
     *
     * @return lista de {@link OrdenTrabajo}
     */
    List<OrdenTrabajo> listarTodas();

    /**
     * Busca una orden de trabajo por su identificador.
     *
     * @param idOrdenTrabajo identificador de la orden (PK de la tabla orden_trabajo).
     * @return la {@link OrdenTrabajo} encontrada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una orden con ese id.
     */
    OrdenTrabajo obtenerPorId(Integer idOrdenTrabajo);

    /**
     * Crea y persiste una nueva orden de trabajo en la base de datos.
     *
     * @param ordenTrabajo entidad {@link OrdenTrabajo} con la información a guardar.
     *                     No debe incluir el id (es autogenerado).
     * @return la orden de trabajo guardada con su id generado.
     */
    OrdenTrabajo crear(OrdenTrabajo ordenTrabajo);

    /**
     * Actualiza los datos de una orden de trabajo existente.
     *
     * @param idOrdenTrabajo        identificador de la orden a actualizar.
     * @param ordenTrabajoActualizada objeto {@link OrdenTrabajo} con los nuevos datos
     *                                (vehiculo, diagnosticoInicial, fechaIngreso,
     *                                 estadoOrden, fechaSalida).
     * @return la orden de trabajo actualizada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una orden con ese id.
     */
    OrdenTrabajo actualizar(Integer idOrdenTrabajo, OrdenTrabajo ordenTrabajoActualizada);

    /**
     * Elimina una orden de trabajo por su identificador.
     *
     * @param idOrdenTrabajo identificador de la orden a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una orden con ese id.
     */
    void eliminar(Integer idOrdenTrabajo);

    // ===== Métodos que envuelven las consultas del OrdenTrabajoRepository =====

    /**
     * Lista todas las órdenes de trabajo asociadas a un vehículo específico.
     *
     * @param idVehiculo identificador del vehículo (placa).
     * @return lista de órdenes de trabajo de ese vehículo.
     */
    List<OrdenTrabajo> listarPorIdVehiculo(String idVehiculo);

    /**
     * Lista las órdenes de trabajo que tienen un estado específico.
     *
     * @param idEstadoOrden identificador del estado de la orden.
     * @return lista de órdenes de trabajo con ese estado.
     */
    List<OrdenTrabajo> listarPorIdEstadoOrden(Byte idEstadoOrden);

    /**
     * Obtiene las órdenes de trabajo cuyo campo fecha_ingreso se encuentra
     * dentro de un rango de fechas.
     *
     * @param inicio fecha de inicio (inclusive).
     * @param fin    fecha de fin (inclusive).
     * @return lista de órdenes de trabajo en ese rango de fechas de ingreso.
     */
    List<OrdenTrabajo> buscarPorRangoFechaIngreso(LocalDate inicio, LocalDate fin);

    /**
     * Obtiene las órdenes de trabajo que aún no tienen fecha de salida,
     * es decir, órdenes abiertas o en proceso.
     *
     * @return lista de órdenes de trabajo sin fecha_salida.
     */
    List<OrdenTrabajo> listarOrdenesAbiertas();

    /**
     * Cuenta cuántas órdenes hay por cada estado, usando la tabla estados_orden.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoOrden (Byte o Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → cantidadOrdenes (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idEstadoOrden, nombreEstado, cantidadOrdenes].
     */
    List<Object[]> contarOrdenesPorEstado();

    /**
     * Cuenta cuántas órdenes de trabajo tiene cada cliente en un rango de fechas
     * de ingreso (usa orden_trabajo.fecha_ingreso).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → cantidadOrdenes (Long o Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idCliente, nombreCliente, cantidadOrdenes].
     */
    List<Object[]> contarOrdenesPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin);

    /**
     * Genera un resumen de costos por orden de trabajo, usando la tabla facturas
     * en un rango de fechas de creación de la factura (facturas.fecha_creacion).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idOrdenTrabajo (Integer o Number)</li>
     *     <li>[1] → fechaIngreso (LocalDate)</li>
     *     <li>[2] → totalFacturado (BigDecimal/Double)</li>
     *     <li>[3] → manoObraTotal (BigDecimal/Double)</li>
     *     <li>[4] → repuestosTotal (BigDecimal/Double)</li>
     *     <li>[5] → impuestoTotal (BigDecimal/Double)</li>
     *     <li>[6] → cantidadFacturas (Long o Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con
     * [idOrdenTrabajo, fechaIngreso, totalFacturado, manoObraTotal,
     *  repuestosTotal, impuestoTotal, cantidadFacturas].
     */
    List<Object[]> obtenerResumenCostosPorOrdenEnRangoFechas(LocalDate inicio, LocalDate fin);
}
