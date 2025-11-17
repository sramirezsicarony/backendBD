package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.CategoriaRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepuestoRepository extends JpaRepository<CategoriaRepuesto, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<CategoriaRepuesto> findById(Integer idCategoria);

    // 2) Listar todas las categorías de repuesto
    @Override
    List<CategoriaRepuesto> findAll();

    // 3) Buscar una categoría por su nombre exacto
    CategoriaRepuesto findByCategoria(String categoria);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar categorías cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<CategoriaRepuesto> findByCategoriaContainingIgnoreCase(String texto);

    // 5) Buscar categorías creadas en un rango de fechas
    List<CategoriaRepuesto> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar categorías ordenadas alfabéticamente por nombre
    List<CategoriaRepuesto> findAllByOrderByCategoriaAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - repuestos (id_categoria_repuesto FK -> categorias_repuesto)
    //  - bodega (id_repuesto FK -> repuestos)

    // 7) Contar cuántos repuestos hay por cada categoría
    //    Devuelve filas: [idCategoria, nombreCategoria, cantidadRepuestos]
    @Query(value = """
            SELECT c.id_categoria                AS idCategoria,
                   c.categoria                   AS nombreCategoria,
                   COUNT(r.id_repuesto)          AS cantidadRepuestos
            FROM categorias_repuesto c
            LEFT JOIN repuestos r
                   ON r.id_categoria_repuesto = c.id_categoria
            GROUP BY c.id_categoria, c.categoria
            ORDER BY cantidadRepuestos DESC
            """, nativeQuery = true)
    List<Object[]> contarRepuestosPorCategoria();

    // 8) Categorías que NO tienen ningún repuesto asociado
    @Query(value = """
            SELECT c.*
            FROM categorias_repuesto c
            LEFT JOIN repuestos r
                   ON r.id_categoria_repuesto = c.id_categoria
            WHERE r.id_repuesto IS NULL
            """, nativeQuery = true)
    List<CategoriaRepuesto> findCategoriasSinRepuestos();

    // 9) Stock total y valor total en bodega por categoría
    //    (usa bodega.stock y bodega.precio_venta)
    //    Devuelve filas: [idCategoria, nombreCategoria, stockTotal, valorTotal]
    @Query(value = """
            SELECT c.id_categoria                            AS idCategoria,
                   c.categoria                               AS nombreCategoria,
                   COALESCE(SUM(b.stock), 0)                 AS stockTotal,
                   COALESCE(SUM(b.stock * b.precio_venta),0) AS valorTotal
            FROM categorias_repuesto c
            JOIN repuestos r
                 ON r.id_categoria_repuesto = c.id_categoria
            JOIN bodega b
                 ON b.id_repuesto = r.id_repuesto
            GROUP BY c.id_categoria, c.categoria
            ORDER BY valorTotal DESC
            """, nativeQuery = true)
    List<Object[]> stockYValorTotalPorCategoriaEnBodega();

}
