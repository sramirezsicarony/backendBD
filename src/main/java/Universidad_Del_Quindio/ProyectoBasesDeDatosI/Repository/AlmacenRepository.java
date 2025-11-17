package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<Almacen> findById(Integer idAlmacen);

    // 2) Listar todos los almacenes
    @Override
    List<Almacen> findAll();

    // 3) Buscar un almacén por su nombre exacto
    Almacen findByNombre(String nombre);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar almacenes cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Almacen> findByNombreContainingIgnoreCase(String texto);

    // 5) Buscar almacenes cuya dirección contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Almacen> findByDireccionContainingIgnoreCase(String texto);

    // 6) Buscar almacenes creados en un rango de fechas
    List<Almacen> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - bodega (id_almacen FK -> almacenes, id_repuesto, stock, precio_venta)

    // 7) Cantidad de repuestos distintos y stock total por almacén
    //    Devuelve filas: [idAlmacen, nombreAlmacen, cantidadRepuestos, stockTotal]
    @Query(value = """
            SELECT a.id_almacen                            AS idAlmacen,
                   a.nombre                                AS nombreAlmacen,
                   COALESCE(COUNT(DISTINCT b.id_repuesto), 0) AS cantidadRepuestos,
                   COALESCE(SUM(b.stock), 0)               AS stockTotal
            FROM almacenes a
            LEFT JOIN bodega b
                   ON b.id_almacen = a.id_almacen
            GROUP BY a.id_almacen, a.nombre
            ORDER BY stockTotal DESC
            """, nativeQuery = true)
    List<Object[]> resumenRepuestosYStockPorAlmacen();

    // 8) Almacenes que NO tienen ningún producto en bodega
    @Query(value = """
            SELECT a.*
            FROM almacenes a
            LEFT JOIN bodega b
                   ON b.id_almacen = a.id_almacen
            WHERE b.id_almacen IS NULL
            """, nativeQuery = true)
    List<Almacen> findAlmacenesSinStock();

    // 9) Stock total y valor total del inventario por almacén
    //    (valorTotal = SUM(stock * precio_venta))
    //    Devuelve filas: [idAlmacen, nombreAlmacen, stockTotal, valorTotal]
    @Query(value = """
            SELECT a.id_almacen                            AS idAlmacen,
                   a.nombre                                AS nombreAlmacen,
                   COALESCE(SUM(b.stock), 0)               AS stockTotal,
                   COALESCE(SUM(b.stock * b.precio_venta), 0) AS valorTotal
            FROM almacenes a
            JOIN bodega b
                 ON b.id_almacen = a.id_almacen
            GROUP BY a.id_almacen, a.nombre
            ORDER BY valorTotal DESC
            """, nativeQuery = true)
    List<Object[]> stockYValorTotalPorAlmacen();

}
