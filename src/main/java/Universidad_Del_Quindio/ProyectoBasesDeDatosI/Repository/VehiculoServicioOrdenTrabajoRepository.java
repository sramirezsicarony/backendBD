package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.VehiculoServicioOrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoServicioOrdenTrabajoRepository extends JpaRepository<VehiculoServicioOrdenTrabajo, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (registro específico de vehículo–servicio–orden)
    @Override
    Optional<VehiculoServicioOrdenTrabajo> findById(Integer idVehiculoServicioOrdenTrabajo);

    // 2) Listar todos los registros de vehículo–servicio–orden
    @Override
    List<VehiculoServicioOrdenTrabajo> findAll();

    // 3) Listar todos los registros de un vehículo (por placa)
    List<VehiculoServicioOrdenTrabajo> findByVehiculo_IdVehiculo(String idVehiculo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Listar todos los registros de un servicio específico
    List<VehiculoServicioOrdenTrabajo> findByServicio_IdServicio(Integer idServicio);

    // 5) Listar todos los registros de una orden de trabajo específica
    List<VehiculoServicioOrdenTrabajo> findByOrdenTrabajo_IdOrdenTrabajo(Integer idOrdenTrabajo);

    // 6) Listar registros ejecutados en un rango de fechas
    List<VehiculoServicioOrdenTrabajo> findByFechaDeEjecucionBetween(LocalDate inicio, LocalDate fin);


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Tablas reales usadas:
    //  - vehiculo_servicio_orden_trabajo (esta)
    //  - servicios (id_servicio, servicio)
    //  - vehiculos (id_vehiculo, id_cliente)
    //  - clientes (id_cliente, nombre)
    //  - orden_trabajo (id_orden_trabajo, fecha_ingreso, fecha_salida)

    // 7) Cantidad de ejecuciones por servicio
    //    Devuelve filas: [idServicio, nombreServicio, cantidadEjecuciones]
    @Query(value = """
            SELECT s.id_servicio                                      AS idServicio,
                   s.servicio                                         AS nombreServicio,
                   COUNT(vso.id_vehiculo_servicio_orden_trabajo)      AS cantidadEjecuciones
            FROM servicios s
            LEFT JOIN vehiculo_servicio_orden_trabajo vso
                   ON vso.id_servicio = s.id_servicio
            GROUP BY s.id_servicio, s.servicio
            ORDER BY cantidadEjecuciones DESC
            """, nativeQuery = true)
    List<Object[]> contarEjecucionesPorServicio();

    // 8) Cantidad de servicios realizados por vehículo en un rango de fechas de ejecución
    //    Devuelve filas: [idVehiculo, cantidadServicios]
    @Query(value = """
            SELECT vso.id_vehiculo                                   AS idVehiculo,
                   COUNT(vso.id_vehiculo_servicio_orden_trabajo)     AS cantidadServicios
            FROM vehiculo_servicio_orden_trabajo vso
            WHERE vso.fecha_de_ejecucion BETWEEN :inicio AND :fin
            GROUP BY vso.id_vehiculo
            ORDER BY cantidadServicios DESC
            """, nativeQuery = true)
    List<Object[]> cantidadServiciosPorVehiculoEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // 9) Servicios realizados por cliente en un rango de fechas de ejecución
    //    Join: clientes -> vehiculos -> vehiculo_servicio_orden_trabajo
    //    Devuelve filas: [idCliente, nombreCliente, cantidadServicios]
    @Query(value = """
            SELECT c.id_cliente                                       AS idCliente,
                   c.nombre                                           AS nombreCliente,
                   COUNT(vso.id_vehiculo_servicio_orden_trabajo)      AS cantidadServicios
            FROM clientes c
            JOIN vehiculos v
                 ON v.id_cliente = c.id_cliente
            JOIN vehiculo_servicio_orden_trabajo vso
                 ON vso.id_vehiculo = v.id_vehiculo
            WHERE vso.fecha_de_ejecucion BETWEEN :inicio AND :fin
            GROUP BY c.id_cliente, c.nombre
            ORDER BY cantidadServicios DESC
            """, nativeQuery = true)
    List<Object[]> serviciosPorClienteEnRangoFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
