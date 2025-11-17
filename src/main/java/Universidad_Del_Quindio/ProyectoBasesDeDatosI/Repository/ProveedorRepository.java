package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<Proveedor> findById(Integer idProveedor);

    // 2) Listar todos los proveedores
    @Override
    List<Proveedor> findAll();

    // 3) Buscar un proveedor por nombre exacto
    Proveedor findByNombre(String nombre);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar proveedores cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Proveedor> findByNombreContainingIgnoreCase(String texto);

    // 5) Buscar proveedores cuya dirección contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Proveedor> findByDireccionContainingIgnoreCase(String texto);

    // 6) Buscar proveedores creados en un rango de fechas
    List<Proveedor> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tabla real usada:
    //  - suministra (id_proveedor FK -> proveedores)
    //    columnas: costo_unitario, cantidad, costo_total, fecha_ingreso

    // 7) Resumen de suministros por proveedor:
    //    cantidad total de unidades y costo total suministrado
    //    Devuelve filas: [idProveedor, nombreProveedor, cantidadTotal, costoTotal]
    @Query(value = """
            SELECT p.id_proveedor                    AS idProveedor,
                   p.nombre                          AS nombreProveedor,
                   COALESCE(SUM(s.cantidad), 0)      AS cantidadTotal,
                   COALESCE(SUM(s.costo_total), 0)   AS costoTotal
            FROM proveedores p
            LEFT JOIN suministra s
                   ON s.id_proveedor = p.id_proveedor
            GROUP BY p.id_proveedor, p.nombre
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> resumenSuministrosPorProveedor();

    // 8) Proveedores que nunca han suministrado ningún repuesto
    @Query(value = """
            SELECT p.*
            FROM proveedores p
            LEFT JOIN suministra s
                   ON s.id_proveedor = p.id_proveedor
            WHERE s.id_proveedor IS NULL
            """, nativeQuery = true)
    List<Proveedor> findProveedoresSinSuministros();

    // 9) Costo total suministrado por proveedor en un rango de fechas
    //    (usa columna fecha_ingreso de suministra)
    //    Devuelve filas: [idProveedor, nombreProveedor, costoTotal]
    @Query(value = """
            SELECT p.id_proveedor                    AS idProveedor,
                   p.nombre                          AS nombreProveedor,
                   COALESCE(SUM(s.costo_total), 0)   AS costoTotal
            FROM proveedores p
            JOIN suministra s
                 ON s.id_proveedor = p.id_proveedor
            WHERE s.fecha_ingreso BETWEEN :inicio AND :fin
            GROUP BY p.id_proveedor, p.nombre
            ORDER BY costoTotal DESC
            """, nativeQuery = true)
    List<Object[]> costoTotalPorProveedorEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
