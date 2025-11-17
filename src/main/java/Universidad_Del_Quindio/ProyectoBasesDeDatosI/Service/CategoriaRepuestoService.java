package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.CategoriaRepuesto;
import java.time.LocalDateTime;
import java.util.List;

public interface CategoriaRepuestoService {

    /**
     * Obtiene la lista completa de categorías de repuesto registradas en el sistema.
     *
     * @return lista de {@link CategoriaRepuesto}
     */
    List<CategoriaRepuesto> listarTodas();

    /**
     * Busca una categoría de repuesto por su identificador.
     *
     * @param idCategoria identificador de la categoría (PK de la tabla categorias_repuesto).
     * @return la {@link CategoriaRepuesto} encontrada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una categoría con ese id.
     */
    CategoriaRepuesto obtenerPorId(Integer idCategoria);

    /**
     * Crea y persiste una nueva categoría de repuesto en la base de datos.
     *
     * @param categoriaRepuesto entidad {@link CategoriaRepuesto} con la información a guardar.
     * @return la categoría de repuesto guardada con su id generado.
     */
    CategoriaRepuesto crear(CategoriaRepuesto categoriaRepuesto);

    /**
     * Actualiza los datos de una categoría de repuesto existente.
     *
     * @param idCategoria              identificador de la categoría a actualizar.
     * @param categoriaRepuestoActualizada objeto {@link CategoriaRepuesto} con los nuevos datos
     *                                     (principalmente el campo categoria).
     * @return la categoría de repuesto actualizada.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una categoría con ese id.
     */
    CategoriaRepuesto actualizar(Integer idCategoria, CategoriaRepuesto categoriaRepuestoActualizada);

    /**
     * Elimina una categoría de repuesto por su identificador.
     *
     * @param idCategoria identificador de la categoría a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe una categoría con ese id.
     */
    void eliminar(Integer idCategoria);

    // ===== Métodos que envuelven las consultas del CategoriaRepuestoRepository =====

    /**
     * Busca una categoría de repuesto por su nombre exacto.
     *
     * @param nombreCategoria nombre exacto de la categoría (columna categoria).
     * @return la categoría encontrada o null si no existe.
     */
    CategoriaRepuesto obtenerPorNombre(String nombreCategoria);

    /**
     * Busca categorías de repuesto cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre de la categoría.
     * @return lista de categorías cuyo nombre contiene el texto dado.
     */
    List<CategoriaRepuesto> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene las categorías de repuesto creadas dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de categorías creadas en ese rango.
     */
    List<CategoriaRepuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todas las categorías de repuesto ordenadas alfabéticamente por el campo categoria.
     *
     * @return lista de categorías ordenadas ascendentemente por nombre.
     */
    List<CategoriaRepuesto> listarOrdenadasPorNombre();

    /**
     * Cuenta cuántos repuestos existen por cada categoría.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCategoria (Integer o Number)</li>
     *     <li>[1] → nombreCategoria (String)</li>
     *     <li>[2] → cantidadRepuestos (Long)</li>
     * </ul>
     *
     * @return lista de registros con la información [idCategoria, nombreCategoria, cantidadRepuestos].
     */
    List<Object[]> contarRepuestosPorCategoria();

    /**
     * Obtiene las categorías de repuesto que no tienen ningún repuesto asociado.
     *
     * @return lista de categorías sin repuestos.
     */
    List<CategoriaRepuesto> listarCategoriasSinRepuestos();

    /**
     * Calcula el stock total y el valor total en bodega por categoría de repuesto,
     * usando las tablas categorias_repuesto, repuestos y bodega.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCategoria (Integer o Number)</li>
     *     <li>[1] → nombreCategoria (String)</li>
     *     <li>[2] → stockTotal (Long o Number)</li>
     *     <li>[3] → valorTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @return lista de registros con [idCategoria, nombreCategoria, stockTotal, valorTotal].
     */
    List<Object[]> obtenerStockYValorTotalPorCategoriaEnBodega();
}
