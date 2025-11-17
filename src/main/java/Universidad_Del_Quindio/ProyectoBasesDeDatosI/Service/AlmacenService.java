package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Almacen;

import java.time.LocalDateTime;
import java.util.List;

public interface AlmacenService {

    /**
     * Obtiene la lista completa de almacenes registrados en el sistema.
     *
     * @return lista de {@link Almacen}
     */
    List<Almacen> listarTodos();

    /**
     * Busca un almacén por su identificador.
     *
     * @param idAlmacen identificador del almacén (PK de la tabla almacenes).
     * @return el {@link Almacen} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un almacén con ese id.
     */
    Almacen obtenerPorId(Integer idAlmacen);

    /**
     * Crea y persiste un nuevo almacén en la base de datos.
     *
     * @param almacen entidad {@link Almacen} con la información a guardar.
     * @return el almacén guardado con su id generado.
     */
    Almacen crear(Almacen almacen);

    /**
     * Actualiza los datos de un almacén existente.
     *
     * @param idAlmacen        identificador del almacén a actualizar.
     * @param almacenActualizado objeto {@link Almacen} con los nuevos datos
     *                           (se usan nombre y direccion).
     * @return el almacén actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un almacén con ese id.
     */
    Almacen actualizar(Integer idAlmacen, Almacen almacenActualizado);

    /**
     * Elimina un almacén por su identificador.
     *
     * @param idAlmacen identificador del almacén a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un almacén con ese id.
     */
    void eliminar(Integer idAlmacen);

    // ===== Métodos que envuelven las consultas del AlmacenRepository =====

    /**
     * Busca un almacén por su nombre exacto.
     *
     * @param nombre nombre exacto del almacén (columna nombre).
     * @return el almacén encontrado o null si no existe.
     */
    Almacen obtenerPorNombre(String nombre);

    /**
     * Busca almacenes cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del almacén.
     * @return lista de almacenes cuyo nombre contiene el texto dado.
     */
    List<Almacen> buscarPorNombreConteniendo(String texto);

    /**
     * Busca almacenes cuya dirección contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro de la dirección.
     * @return lista de almacenes cuya dirección contiene el texto dado.
     */
    List<Almacen> buscarPorDireccionConteniendo(String texto);

    /**
     * Obtiene los almacenes creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de almacenes creados en ese rango.
     */
    List<Almacen> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene un resumen de repuestos e inventario por almacén:
     * cantidad de repuestos distintos y stock total.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idAlmacen (Integer o Number)</li>
     *     <li>[1] → nombreAlmacen (String)</li>
     *     <li>[2] → cantidadRepuestos (Long)</li>
     *     <li>[3] → stockTotal (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idAlmacen, nombreAlmacen, cantidadRepuestos, stockTotal].
     */
    List<Object[]> obtenerResumenRepuestosYStockPorAlmacen();

    /**
     * Obtiene los almacenes que no tienen ningún producto en bodega
     * (sin registros en la tabla bodega).
     *
     * @return lista de almacenes sin stock.
     */
    List<Almacen> listarAlmacenesSinStock();

    /**
     * Calcula el stock total y el valor total del inventario por almacén,
     * usando la tabla bodega (stock * precio_venta).
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
}
