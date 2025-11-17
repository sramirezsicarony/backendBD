package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Bodega;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.BodegaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, BodegaId> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar registro de bodega por su ID compuesto (id_almacen, id_repuesto)
    @Override
    Optional<Bodega> findById(BodegaId id);

    // 2) Listar todos los registros de bodega
    @Override
    List<Bodega> findAll();

    // 3) Listar todos los registros de bodega de un almacén específico
    List<Bodega> findByAlmacen_IdAlmacen(Integer idAlmacen);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar registros con stock mayor o igual a un valor
    List<Bodega> findByStockGreaterThanEqual(Integer stockMinimo);

    // 5) Buscar registros con precio_venta en un rango
    List<Bodega> findByPrecioVentaBetween(BigDecimal precioMin, BigDecimal precioMax);

    // 6) Buscar registros creados en un rango de fechas
    List<Bodega> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - almacenes (id_almacen PK, nombre, direccion)
    //  - repuestos (id_repuesto PK, nombre, id_categoria_repuesto)
    //  - categorias_repuesto (id_categoria PK, categoria)

    // 7) Stock total y valor total del inventario por almacén
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

    // 8) Stock total y valor total por repuesto a nivel global (todos los almacenes)
    //    Devuelve filas: [idRepuesto, nombreRepuesto, stockTotal, valorTotal]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(b.stock), 0)               AS stockTotal,
                   COALESCE(SUM(b.stock * b.precio_venta), 0) AS valorTotal
            FROM repuestos r
            JOIN bodega b
                 ON b.id_repuesto = r.id_repuesto
            GROUP BY r.id_repuesto, r.nombre
            ORDER BY stockTotal DESC
            """, nativeQuery = true)
    List<Object[]> stockYValorTotalPorRepuestoGlobal();

    // 9) Repuestos con stock global por debajo de un umbral (en todos los almacenes)
    //    Devuelve filas: [idRepuesto, nombreRepuesto, stockTotal]
    @Query(value = """
            SELECT r.id_repuesto                           AS idRepuesto,
                   r.nombre                                AS nombreRepuesto,
                   COALESCE(SUM(b.stock), 0)               AS stockTotal
            FROM repuestos r
            JOIN bodega b
                 ON b.id_repuesto = r.id_repuesto
            GROUP BY r.id_repuesto, r.nombre
            HAVING stockTotal < :umbral
            ORDER BY stockTotal ASC
            """, nativeQuery = true)
    List<Object[]> repuestosConStockGlobalBajo(@Param("umbral") Integer umbral);

}
