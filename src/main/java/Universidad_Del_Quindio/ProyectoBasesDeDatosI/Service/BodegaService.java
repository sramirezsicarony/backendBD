package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Bodega;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.BodegaId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BodegaService {

    /**
     * Obtiene la lista completa de registros de bodega (inventario) en el sistema.
     *
     * @return lista de {@link Bodega}
     */
    List<Bodega> listarTodos();

    /**
     * Busca un registro de bodega por su ID compuesto (id_almacen, id_repuesto).
     *
     * @param id objeto {@link BodegaId} que contiene id_almacen e id_repuesto.
     * @return el registro de {@link Bodega} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    Bodega obtenerPorId(BodegaId id);

    /**
     * Crea y persiste un nuevo registro de bodega.
     * <p>
     * Nota: el {@link BodegaId} se construye a partir de los IDs de
     * {@link Bodega#getAlmacen()} y {@link Bodega#getRepuesto()}, si aún es null.
     *
     * @param bodega entidad {@link Bodega} con la información a guardar
     *               (almacén, repuesto, stock, precioVenta).
     * @return el registro de bodega guardado.
     */
    Bodega crear(Bodega bodega);

    /**
     * Actualiza los datos de un registro de bodega existente.
     *
     * @param id                identificador compuesto del registro (almacén + repuesto).
     * @param bodegaActualizada objeto {@link Bodega} con los nuevos datos
     *                          (se usan principalmente stock y precioVenta).
     * @return el registro de bodega actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    Bodega actualizar(BodegaId id, Bodega bodegaActualizada);

    /**
     * Elimina un registro de bodega por su ID compuesto (almacén + repuesto).
     *
     * @param id identificador compuesto {@link BodegaId} del registro a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    void eliminar(BodegaId id);

    // ===== Métodos que envuelven las consultas del BodegaRepository =====

    /**
     * Lista todos los registros de bodega asociados a un almacén específico.
     *
     * @param idAlmacen identificador del almacén.
     * @return lista de registros de bodega para ese almacén.
     */
    List<Bodega> listarPorIdAlmacen(Integer idAlmacen);

    /**
     * Busca registros de bodega cuyo stock sea mayor o igual al valor indicado.
     *
     * @param stockMinimo stock mínimo (inclusive).
     * @return lista de registros de bodega que cumplen con el criterio.
     */
    List<Bodega> buscarPorStockMayorIgual(Integer stockMinimo);

    /**
     * Busca registros de bodega cuyo precio de venta se encuentre dentro de un rango.
     *
     * @param precioMin precio mínimo (inclusive).
     * @param precioMax precio máximo (inclusive).
     * @return lista de registros cuyo precio_venta está en ese rango.
     */
    List<Bodega> buscarPorRangoPrecioVenta(BigDecimal precioMin, BigDecimal precioMax);

    /**
     * Obtiene los registros de bodega creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de registros de bodega creados en ese rango.
     */
    List<Bodega> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula el stock total y el valor total del inventario por almacén,
     * usando la tabla bodega y la tabla almacenes.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAlmacen (Integer o Number)</li>
     *     <li>[1] → nombreAlmacen (String)</li>
     *     <li>[2] → stockTotal (Long o Number)</li>
     *     <li>[3] → valorTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idAlmacen, nombreAlmacen, stockTotal, valorTotal].
     */
    List<Object[]> obtenerStockYValorTotalPorAlmacen();

    /**
     * Calcula el stock total y el valor total por repuesto a nivel global
     * (sumando todos los almacenes).
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
    List<Object[]> obtenerStockYValorTotalPorRepuestoGlobal();

    /**
     * Obtiene los repuestos cuyo stock global (sumando todos los almacenes)
     * está por debajo de un umbral dado.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idRepuesto (Integer o Number)</li>
     *     <li>[1] → nombreRepuesto (String)</li>
     *     <li>[2] → stockTotal (Long o Number)</li>
     * </ul>
     *
     * @param umbral valor máximo de stock global para considerar que está “bajo”.
     * @return lista de registros con [idRepuesto, nombreRepuesto, stockTotal].
     */
    List<Object[]> obtenerRepuestosConStockGlobalBajo(Integer umbral);
}
