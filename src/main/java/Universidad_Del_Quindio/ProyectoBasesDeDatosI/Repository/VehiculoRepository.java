package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar vehículo por placa (id_vehiculo)
    @Override
    Optional<Vehiculo> findById(String idVehiculo);

    // 2) Listar todos los vehículos
    @Override
    List<Vehiculo> findAll();

    // 3) Listar vehículos de un cliente (por id_cliente / DNI)
    List<Vehiculo> findByCliente_IdCliente(String idCliente);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar vehículos por marca (contiene, ignorando mayúsculas)
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);

    // 5) Buscar vehículos por modelo (contiene, ignorando mayúsculas)
    List<Vehiculo> findByModeloContainingIgnoreCase(String modelo);

    // 6) Buscar vehículos por rango de años (anio BETWEEN ...)
    List<Vehiculo> findByAnioBetween(Short anioInicio, Short anioFin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - orden_trabajo (id_vehiculo FK -> vehiculos, fecha_ingreso, fecha_salida, id_estado_orden)
    //  - vehiculo_servicio_orden_trabajo (id_vehiculo FK -> vehiculos, id_servicio, fecha_de_ejecucion)
    //  - facturas (id_orden_trabajo FK -> orden_trabajo, total, fecha_creacion)

    // 7) Cantidad de órdenes de trabajo por vehículo
    //    Devuelve filas: [idVehiculo, marca, modelo, anio, cantidadOrdenes]
    @Query(value = """
            SELECT v.id_vehiculo                      AS idVehiculo,
                   v.marca                            AS marca,
                   v.modelo                           AS modelo,
                   v.anio                             AS anio,
                   COUNT(ot.id_orden_trabajo)         AS cantidadOrdenes
            FROM vehiculos v
            LEFT JOIN orden_trabajo ot
                   ON ot.id_vehiculo = v.id_vehiculo
            GROUP BY v.id_vehiculo, v.marca, v.modelo, v.anio
            ORDER BY cantidadOrdenes DESC
            """, nativeQuery = true)
    List<Object[]> contarOrdenesPorVehiculo();

    // 8) Vehículos que nunca han tenido una orden de trabajo
    @Query(value = """
            SELECT v.*
            FROM vehiculos v
            LEFT JOIN orden_trabajo ot
                   ON ot.id_vehiculo = v.id_vehiculo
            WHERE ot.id_orden_trabajo IS NULL
            """, nativeQuery = true)
    List<Vehiculo> findVehiculosSinOrdenes();

    // 9) Total facturado por vehículo en un rango de fechas de facturas
    //    Join: vehiculos -> orden_trabajo -> facturas
    //    Devuelve filas: [idVehiculo, marca, modelo, anio, totalFacturado, cantidadFacturas]
    @Query(value = """
            SELECT v.id_vehiculo                      AS idVehiculo,
                   v.marca                            AS marca,
                   v.modelo                           AS modelo,
                   v.anio                             AS anio,
                   COALESCE(SUM(f.total), 0)          AS totalFacturado,
                   COUNT(f.id_factura)                AS cantidadFacturas
            FROM vehiculos v
            JOIN orden_trabajo ot
                 ON ot.id_vehiculo = v.id_vehiculo
            JOIN facturas f
                 ON f.id_orden_trabajo = ot.id_orden_trabajo
            WHERE f.fecha_creacion BETWEEN :inicio AND :fin
            GROUP BY v.id_vehiculo, v.marca, v.modelo, v.anio
            ORDER BY totalFacturado DESC
            """, nativeQuery = true)
    List<Object[]> totalFacturadoPorVehiculoEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
