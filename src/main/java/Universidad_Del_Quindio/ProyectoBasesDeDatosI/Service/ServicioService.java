package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ServicioService {

    /**
     * Obtiene la lista completa de servicios registrados en el sistema.
     *
     * @return lista de {@link Servicio}
     */
    List<Servicio> listarTodos();

    /**
     * Busca un servicio por su identificador.
     *
     * @param idServicio identificador del servicio (PK de la tabla servicios).
     * @return el {@link Servicio} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un servicio con ese id.
     */
    Servicio obtenerPorId(Integer idServicio);

    /**
     * Crea y persiste un nuevo servicio en la base de datos.
     *
     * @param servicio entidad {@link Servicio} con la información a guardar.
     * @return el servicio guardado con su id generado.
     */
    Servicio crear(Servicio servicio);

    /**
     * Actualiza los datos de un servicio existente.
     *
     * @param idServicio          identificador del servicio a actualizar.
     * @param servicioActualizado objeto {@link Servicio} con los nuevos datos
     *                            (se usan servicio y descripcion).
     * @return el servicio actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un servicio con ese id.
     */
    Servicio actualizar(Integer idServicio, Servicio servicioActualizado);

    /**
     * Elimina un servicio por su identificador.
     *
     * @param idServicio identificador del servicio a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un servicio con ese id.
     */
    void eliminar(Integer idServicio);

    // ===== Métodos que envuelven las consultas del ServicioRepository =====

    /**
     * Busca un servicio por su nombre exacto.
     *
     * @param nombreServicio nombre exacto del servicio (columna servicio).
     * @return el servicio encontrado o null si no existe.
     */
    Servicio obtenerPorNombre(String nombreServicio);

    /**
     * Busca servicios cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del servicio.
     * @return lista de servicios cuyo nombre contiene el texto dado.
     */
    List<Servicio> buscarPorNombreConteniendo(String texto);

    /**
     * Obtiene los servicios creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de servicios creados en ese rango.
     */
    List<Servicio> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Lista todos los servicios ordenados alfabéticamente por el campo servicio.
     *
     * @return lista de servicios ordenados ascendentemente por nombre.
     */
    List<Servicio> listarOrdenadosPorNombre();

    /**
     * Cuenta cuántas veces se ha ejecutado cada servicio, de acuerdo a la tabla
     * vehiculo_servicio_orden_trabajo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idServicio (Integer o Number)</li>
     *     <li>[1] → nombreServicio (String)</li>
     *     <li>[2] → cantidadEjecuciones (Long)</li>
     * </ul>
     *
     * @return lista de registros con [idServicio, nombreServicio, cantidadEjecuciones].
     */
    List<Object[]> contarEjecucionesPorServicio();

    /**
     * Obtiene los servicios que nunca se han ejecutado
     * (no tienen registros en vehiculo_servicio_orden_trabajo).
     *
     * @return lista de servicios nunca ejecutados.
     */
    List<Servicio> listarServiciosNuncaEjecutados();

    /**
     * Obtiene información de uso de servicios en un rango de fechas de ejecución,
     * tomando la columna fecha_de_ejecucion de vehiculo_servicio_orden_trabajo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idServicio (Integer o Number)</li>
     *     <li>[1] → nombreServicio (String)</li>
     *     <li>[2] → ejecuciones (Long)</li>
     *     <li>[3] → vehiculosDistintos (Long)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idServicio, nombreServicio, ejecuciones, vehiculosDistintos].
     */
    List<Object[]> obtenerUsoDeServiciosEnRangoFechas(LocalDate inicio, LocalDate fin);
}
