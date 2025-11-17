package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Integer> {

    // ================== CONSULTAS SIMPLES ==================

    // 1) Buscar por id (lectura puntual)
    @Override
    Optional<TipoVehiculo> findById(Integer idTipoVehiculo);

    // 2) Listar todos los tipos de vehículo
    @Override
    List<TipoVehiculo> findAll();

    // 3) Buscar un tipo de vehículo por su nombre exacto
    TipoVehiculo findByTipo(String tipo);


    // ================== CONSULTAS INTERMEDIAS ==================

    // 4) Buscar tipos de vehículo cuyo nombre contenga cierto texto (LIKE, ignorando mayúsculas)
    List<TipoVehiculo> findByTipoContainingIgnoreCase(String texto);

    // 5) Buscar tipos de vehículo creados en un rango de fechas
    List<TipoVehiculo> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);

    // 6) Listar tipos de vehículo ordenados alfabéticamente por nombre
    List<TipoVehiculo> findAllByOrderByTipoAsc();


    // ================== CONSULTAS COMPLEJAS (JOIN / AGREGACIONES) ==================
    // Usan la tabla REAL: vehiculos (id_tipo_vehiculo FK a tipo_vehiculo)

    // 7) Contar cuántos vehículos hay por cada tipo
    //    Devuelve filas: [idTipoVehiculo, nombreTipo, cantidadVehiculos]
    @Query(value = """
            SELECT tv.id_tipo_vehiculo          AS idTipoVehiculo,
                   tv.tipo                      AS nombreTipo,
                   COUNT(v.id_vehiculo)         AS cantidadVehiculos
            FROM tipo_vehiculo tv
            LEFT JOIN vehiculos v
                   ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo
            GROUP BY tv.id_tipo_vehiculo, tv.tipo
            ORDER BY cantidadVehiculos DESC
            """, nativeQuery = true)
    List<Object[]> contarVehiculosPorTipo();

    // 8) Tipos de vehículo que NO tienen ningún vehículo registrado
    @Query(value = """
            SELECT tv.*
            FROM tipo_vehiculo tv
            LEFT JOIN vehiculos v
                   ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo
            WHERE v.id_vehiculo IS NULL
            """, nativeQuery = true)
    List<TipoVehiculo> findTiposSinVehiculos();

    // 9) Cantidad de vehículos por tipo en un rango de años de modelo
    //    Devuelve filas: [idTipoVehiculo, nombreTipo, cantidadVehiculos]
    @Query(value = """
            SELECT tv.id_tipo_vehiculo          AS idTipoVehiculo,
                   tv.tipo                      AS nombreTipo,
                   COUNT(v.id_vehiculo)         AS cantidadVehiculos
            FROM tipo_vehiculo tv
            JOIN vehiculos v
                 ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo
            WHERE v.anio BETWEEN :anioInicio AND :anioFin
            GROUP BY tv.id_tipo_vehiculo, tv.tipo
            ORDER BY cantidadVehiculos DESC
            """, nativeQuery = true)
    List<Object[]> contarVehiculosPorTipoEnRangoAnio(
            @Param("anioInicio") int anioInicio,
            @Param("anioFin") int anioFin
    );

}
