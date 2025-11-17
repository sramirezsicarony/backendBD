package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (DNI del cliente)
    @Override
    Optional<Cliente> findById(String idCliente);

    // 2) Listar todos los clientes
    @Override
    List<Cliente> findAll();

    // 3) Buscar un cliente por su correo exacto
    Cliente findByCorreo(String correo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar clientes cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<Cliente> findByNombreContainingIgnoreCase(String texto);

    // 5) Buscar clientes cuyo teléfono comience por un prefijo (ej: indicativo)
    List<Cliente> findByTelefonoStartingWith(String prefijo);

    // 6) Buscar clientes creados en un rango de fechas (según created_at)
    List<Cliente> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - vehiculos (id_cliente FK -> clientes)
    //  - facturas (id_cliente FK -> clientes, fecha_creacion, total, sub_total_mano_de_obra, sub_total_repuestos, impuesto)

    // 7) Cantidad de vehículos registrados por cliente
    //    Devuelve filas: [idCliente, nombreCliente, cantidadVehiculos]
    @Query(value = """
            SELECT c.id_cliente                    AS idCliente,
                   c.nombre                        AS nombreCliente,
                   COUNT(v.id_vehiculo)            AS cantidadVehiculos
            FROM clientes c
            LEFT JOIN vehiculos v
                   ON v.id_cliente = c.id_cliente
            GROUP BY c.id_cliente, c.nombre
            ORDER BY cantidadVehiculos DESC
            """, nativeQuery = true)
    List<Object[]> contarVehiculosPorCliente();

    // 8) Clientes que NO tienen vehículos registrados
    @Query(value = """
            SELECT c.*
            FROM clientes c
            LEFT JOIN vehiculos v
                   ON v.id_cliente = c.id_cliente
            WHERE v.id_vehiculo IS NULL
            """, nativeQuery = true)
    List<Cliente> findClientesSinVehiculos();

    // 9) Resumen de facturación por cliente en un rango de fechas
    //    (usa facturas.fecha_creacion)
    //    Devuelve filas: [idCliente, nombreCliente, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal]
    @Query(value = """
            SELECT c.id_cliente                                 AS idCliente,
                   c.nombre                                     AS nombreCliente,
                   COALESCE(SUM(f.total), 0)                    AS totalFacturado,
                   COALESCE(SUM(f.sub_total_mano_de_obra), 0)   AS manoObraTotal,
                   COALESCE(SUM(f.sub_total_repuestos), 0)      AS repuestosTotal,
                   COALESCE(SUM(f.impuesto), 0)                 AS impuestoTotal
            FROM clientes c
            JOIN facturas f
                 ON f.id_cliente = c.id_cliente
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY c.id_cliente, c.nombre
            ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<Object[]> resumenFacturacionPorClienteEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
