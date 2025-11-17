package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Proveedor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProveedorService {

    /**
     * Obtiene la lista completa de proveedores registrados en el sistema.
     *
     * @return lista de {@link Proveedor}
     */
    List<Proveedor> listarTodos();

    /**
     * Busca un proveedor por su identificador.
     *
     * @param idProveedor identificador del proveedor (PK de la tabla proveedores).
     * @return el {@link Proveedor} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un proveedor con ese id.
     */
    Proveedor obtenerPorId(Integer idProveedor);

    /**
     * Crea y persiste un nuevo proveedor en la base de datos.
     *
     * @param proveedor entidad {@link Proveedor} con la información a guardar.
     * @return el proveedor guardado con su id generado.
     */
    Proveedor crear(Proveedor proveedor);

    /**
     * Actualiza los datos de un proveedor existente.
     *
     * @param idProveedor          identificador del proveedor a actualizar.
     * @param proveedorActualizado objeto {@link Proveedor} con los nuevos datos
     *                             (se usan nombre, teléfono y dirección).
     * @return el proveedor actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un proveedor con ese id.
     */
    Proveedor actualizar(Integer idProveedor, Proveedor proveedorActualizado);

    /**
     * Elimina un proveedor por su identificador.
     *
     * @param idProveedor identificador del proveedor a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un proveedor con ese id.
     */
    void eliminar(Integer idProveedor);

    // ===== Métodos que envuelven las consultas del ProveedorRepository =====

    /**
     * Busca un proveedor por su nombre exacto.
     *
     * @param nombre nombre exacto del proveedor (columna nombre).
     * @return el proveedor encontrado o null si no existe.
     */
    Proveedor obtenerPorNombre(String nombre);

    /**
     * Busca proveedores cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del proveedor.
     * @return lista de proveedores cuyo nombre contiene el texto dado.
     */
    List<Proveedor> buscarPorNombreConteniendo(String texto);

    /**
     * Busca proveedores cuya dirección contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro de la dirección.
     * @return lista de proveedores cuya dirección contiene el texto dado.
     */
    List<Proveedor> buscarPorDireccionConteniendo(String texto);

    /**
     * Obtiene los proveedores creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de proveedores creados en ese rango.
     */
    List<Proveedor> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene un resumen de los suministros por proveedor:
     * cantidad total de unidades suministradas y costo total.
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
    List<Object[]> obtenerResumenSuministrosPorProveedor();

    /**
     * Obtiene los proveedores que nunca han suministrado ningún repuesto
     * (no tienen registros en la tabla suministra).
     *
     * @return lista de proveedores sin suministros.
     */
    List<Proveedor> listarProveedoresSinSuministros();

    /**
     * Calcula el costo total suministrado por proveedor en un rango de fechas,
     * usando la columna fecha_ingreso de la tabla suministra.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idProveedor (Integer o Number)</li>
     *     <li>[1] → nombreProveedor (String)</li>
     *     <li>[2] → costoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idProveedor, nombreProveedor, costoTotal].
     */
    List<Object[]> obtenerCostoTotalPorProveedorEnRangoFechas(LocalDate inicio, LocalDate fin);
}
