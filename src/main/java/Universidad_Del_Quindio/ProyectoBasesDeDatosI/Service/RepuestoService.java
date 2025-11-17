package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Repuesto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RepuestoService {

    /**
     * Obtiene la lista completa de repuestos registrados en el sistema.
     *
     * @return lista de {@link Repuesto}
     */
    List<Repuesto> listarTodos();

    /**
     * Busca un repuesto por su identificador.
     *
     * @param idRepuesto identificador del repuesto (PK de la tabla repuestos).
     * @return el {@link Repuesto} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un repuesto con ese id.
     */
    Repuesto obtenerPorId(Integer idRepuesto);

    /**
     * Crea y persiste un nuevo repuesto en la base de datos.
     *
     * @param repuesto entidad {@link Repuesto} con la información a guardar.
     * @return el repuesto guardado con su id generado.
     */
    Repuesto crear(Repuesto repuesto);

    /**
     * Actualiza los datos de un repuesto existente.
     *
     * @param idRepuesto        identificador del repuesto a actualizar.
     * @param repuestoActualizado objeto {@link Repuesto} con los nuevos datos
     *                            (se usan nombre, descripción y categoría).
     * @return el repuesto actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un repuesto con ese id.
     */
    Repuesto actualizar(Integer idRepuesto, Repuesto repuestoActualizado);

    /**
     * Elimina un repuesto por su identificador.
     *
     * @param idRepuesto identificador del repuesto a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un repuesto con ese id.
     */
    void eliminar(Integer idRepuesto);

    // ===== Métodos que envuelven las consultas del RepuestoRepository =====

    /**
     * Busca un repuesto por su nombre exacto.
     *
     * @param nombre nombre exacto del repuesto (columna nombre).
     * @return el repuesto encontrado o null si no existe.
     */
    Repuesto obtenerPorNombre(String nombre);

    /**
     * Busca repuestos cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del repuesto.
     * @return lista de repuestos cuyo nombre contiene el texto dado.
     */
    List<Repuesto> buscarPorNombreConteniendo(String texto);

    /**
     * Lista los repuestos que pertenecen a una categoría específica.
     *
     * @param idCategoria identificador de la categoría de repuesto.
     * @return lista de repuestos asociados a esa categoría.
     */
    List<Repuesto> listarPorIdCategoria(Integer idCategoria);

    /**
     * Obtiene los repuestos creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de repuestos creados en ese rango.
     */
    List<Repuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene el stock total y el valor total en todos los almacenes para cada repuesto,
     * usando la tabla bodega.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → stockTotal (Long o Number)</li>
     *     <li>[3] → valorTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idRepuesto, nombreRepuesto, stockTotal, valorTotal].
     */
    List<Object[]> obtenerStockYValorTotalPorRepuesto();

    /**
     * Calcula el total vendido por repuesto en un rango de fechas, según la tabla
     * detalle_orden_repuesto (usa created_at).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → cantidadTotal (Long o Number)</li>
     *     <li>[3] → totalVendido (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha/hora de inicio del rango (inclusive).
     * @param fin    fecha/hora de fin del rango (inclusive).
     * @return lista de registros con [idRepuesto, nombreRepuesto, cantidadTotal, totalVendido].
     */
    List<Object[]> obtenerVentasPorRepuestoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula el total comprado por repuesto en un rango de fechas, según la tabla
     * suministra (usa fecha_ingreso).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → cantidadComprada (Long o Number)</li>
     *     <li>[3] → costoTotalCompras (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idRepuesto, nombreRepuesto, cantidadComprada, costoTotalCompras].
     */
    List<Object[]> obtenerComprasPorRepuestoEnRangoFechas(LocalDate inicio, LocalDate fin);
}
