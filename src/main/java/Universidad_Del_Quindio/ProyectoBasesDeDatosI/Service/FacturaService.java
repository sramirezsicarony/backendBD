package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Factura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FacturaService {

    /**
     * Obtiene la lista completa de facturas.
     *
     * @return lista de {@link Factura}
     */
    List<Factura> listarTodas();

    /**
     * Busca una factura por su identificador.
     *
     * @param idFactura identificador de la factura (PK).
     * @return la factura encontrada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una factura con ese id.
     */
    Factura obtenerPorId(Integer idFactura);

    /**
     * Crea y persiste una nueva factura en la base de datos.
     *
     * @param factura entidad {@link Factura} con la información a guardar
     *                (ordenTrabajo, estadoFactura, cliente, fechaCreacion,
     *                 subTotalManoDeObra, subTotalRepuestos, impuesto, total).
     * @return la factura guardada con su id generado.
     */
    Factura crear(Factura factura);

    /**
     * Actualiza los datos de una factura existente.
     *
     * @param idFactura       identificador de la factura a actualizar.
     * @param facturaActualizada objeto {@link Factura} con los nuevos datos
     *                           (relaciones y valores numéricos).
     * @return la factura actualizada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una factura con ese id.
     */
    Factura actualizar(Integer idFactura, Factura facturaActualizada);

    /**
     * Elimina una factura por su identificador.
     *
     * @param idFactura identificador de la factura a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una factura con ese id.
     */
    void eliminar(Integer idFactura);

    // ===== Métodos que envuelven las consultas del Repository =====

    /**
     * Lista todas las facturas asociadas a un cliente específico.
     *
     * @param idCliente identificador del cliente (DNI).
     * @return lista de facturas de ese cliente.
     */
    List<Factura> listarPorIdCliente(String idCliente);

    /**
     * Lista todas las facturas con un estado específico.
     *
     * @param idEstadoFactura identificador del estado de la factura.
     * @return lista de facturas en ese estado.
     */
    List<Factura> listarPorIdEstadoFactura(Byte idEstadoFactura);

    /**
     * Obtiene las facturas cuya fecha de creación esté dentro de un rango.
     *
     * @param inicio fecha de inicio (inclusive).
     * @param fin    fecha de fin (inclusive).
     * @return lista de facturas en ese rango de fechas.
     */
    List<Factura> buscarPorFechaCreacionEntre(LocalDate inicio, LocalDate fin);

    /**
     * Obtiene las facturas cuyo total esté dentro de un rango.
     *
     * @param totalMin valor mínimo del total (inclusive).
     * @param totalMax valor máximo del total (inclusive).
     * @return lista de facturas que cumplen el criterio.
     */
    List<Factura> buscarPorTotalEntre(BigDecimal totalMin, BigDecimal totalMax);

    /**
     * Resumen de facturación por cliente en un rango de fechas.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → totalFacturado (Number, BigDecimal/Double)</li>
     *     <li>[3] → manoObraTotal (Number)</li>
     *     <li>[4] → repuestosTotal (Number)</li>
     *     <li>[5] → impuestoTotal (Number)</li>
     *     <li>[6] → cantidadFacturas (Long/Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de filas con el resumen por cliente.
     */
    List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin);

    /**
     * Total facturado por estado de factura en un rango de fechas.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idEstadoFactura (Byte/Number)</li>
     *     <li>[1] → nombreEstado (String)</li>
     *     <li>[2] → totalFacturado (Number)</li>
     *     <li>[3] → cantidadFacturas (Long/Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de filas con el resumen por estado.
     */
    List<Object[]> obtenerTotalFacturadoPorEstadoEnRangoFechas(LocalDate inicio, LocalDate fin);

    /**
     * Facturación mensual global en un rango de fechas.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → anio (Integer/Number)</li>
     *     <li>[1] → mes (Integer/Number, 1–12)</li>
     *     <li>[2] → totalFacturado (Number)</li>
     *     <li>[3] → manoObraTotal (Number)</li>
     *     <li>[4] → repuestosTotal (Number)</li>
     *     <li>[5] → impuestoTotal (Number)</li>
     *     <li>[6] → cantidadFacturas (Long/Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de filas con la facturación agrupada por año/mes.
     */
    List<Object[]> obtenerFacturacionMensualEnRangoFechas(LocalDate inicio, LocalDate fin);
}
