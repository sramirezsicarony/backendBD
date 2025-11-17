package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Suministra;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.SuministraId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SuministraService {

    /**
     * Obtiene la lista completa de registros de suministros.
     *
     * @return lista de {@link Suministra}
     */
    List<Suministra> listarTodos();

    /**
     * Busca un registro de suministro por su ID compuesto
     * (id_proveedor, id_repuesto).
     *
     * @param id objeto {@link SuministraId} que contiene id_proveedor e id_repuesto.
     * @return el registro de {@link Suministra} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    Suministra obtenerPorId(SuministraId id);

    /**
     * Crea y persiste un nuevo registro de suministro.
     * <p>
     * Nota: el {@link SuministraId} se construye a partir de los IDs de
     * {@link Suministra#getProveedor()} y {@link Suministra#getRepuesto()}, si aún es null.
     *
     * @param suministra entidad {@link Suministra} con la información a guardar
     *                   (proveedor, repuesto, costos, cantidad, fechaIngreso).
     * @return el registro de suministro guardado.
     */
    Suministra crear(Suministra suministra);

    /**
     * Actualiza los datos de un registro de suministro existente.
     *
     * @param id                    identificador compuesto del registro (proveedor + repuesto).
     * @param suministraActualizada objeto {@link Suministra} con los nuevos datos
     *                              (costoUnitario, cantidad, costoTotal, fechaIngreso).
     * @return el registro de suministro actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    Suministra actualizar(SuministraId id, Suministra suministraActualizada);

    /**
     * Elimina un registro de suministro por su ID compuesto (proveedor + repuesto).
     *
     * @param id identificador compuesto {@link SuministraId} del registro a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    void eliminar(SuministraId id);

    // ===== Métodos que envuelven las consultas del SuministraRepository =====

    /**
     * Lista todos los suministros realizados por un proveedor específico.
     *
     * @param idProveedor identificador del proveedor.
     * @return lista de registros de suministro de ese proveedor.
     */
    List<Suministra> listarPorIdProveedor(Integer idProveedor);

    /**
     * Lista todos los suministros relacionados con un repuesto específico.
     *
     * @param idRepuesto identificador del repuesto.
     * @return lista de registros de suministro del repuesto indicado.
     */
    List<Suministra> listarPorIdRepuesto(Integer idRepuesto);

    /**
     * Obtiene los suministros cuyo campo fecha_ingreso está dentro de un rango.
     *
     * @param inicio fecha de inicio (inclusive).
     * @param fin    fecha de fin (inclusive).
     * @return lista de registros de suministro en ese rango de fechas.
     */
    List<Suministra> buscarPorRangoFechaIngreso(LocalDate inicio, LocalDate fin);

    /**
     * Busca suministros cuyo costo unitario se encuentre dentro de un rango.
     *
     * @param costoMin costo unitario mínimo (inclusive).
     * @param costoMax costo unitario máximo (inclusive).
     * @return lista de registros cuyo costo_unitario está en ese rango.
     */
    List<Suministra> buscarPorRangoCostoUnitario(BigDecimal costoMin, BigDecimal costoMax);

    /**
     * Busca suministros cuya cantidad sea mayor o igual a un valor dado.
     *
     * @param cantidadMinima cantidad mínima (inclusive).
     * @return lista de registros de suministro que cumplen con el criterio.
     */
    List<Suministra> buscarPorCantidadMayorIgual(Integer cantidadMinima);

    /**
     * Obtiene los suministros creados (created_at) dentro de un rango de timestamps.
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de suministros creados en ese rango.
     */
    List<Suministra> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Genera un resumen global de compras por proveedor, usando la tabla suministra:
     * cantidad total y costo total.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idProveedor (Integer o Number)</li>
     *     <li>[1] → nombreProveedor (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → costoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idProveedor, nombreProveedor, cantidadTotal, costoTotal].
     */
    List<Object[]> obtenerResumenComprasPorProveedor();

    /**
     * Genera un resumen global de compras por repuesto, usando la tabla suministra:
     * cantidad total y costo total.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → costoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idRepuesto, nombreRepuesto, cantidadTotal, costoTotal].
     */
    List<Object[]> obtenerResumenComprasPorRepuesto();

    /**
     * Calcula las compras por proveedor en un rango de fechas de ingreso,
     * usando la columna fecha_ingreso.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idProveedor (Integer o Number)</li>
     *     <li>[1] → nombreProveedor (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → costoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idProveedor, nombreProveedor, cantidadTotal, costoTotal].
     */
    List<Object[]> obtenerComprasPorProveedorEnRangoFechas(LocalDate inicio, LocalDate fin);
}
