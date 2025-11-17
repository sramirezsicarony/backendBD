package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.VehiculoServicioOrdenTrabajo;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoServicioOrdenTrabajoService {

    /**
     * Obtiene la lista completa de registros vehículo–servicio–orden.
     *
     * @return lista de {@link VehiculoServicioOrdenTrabajo}
     */
    List<VehiculoServicioOrdenTrabajo> listarTodos();

    /**
     * Busca un registro vehículo–servicio–orden por su identificador.
     *
     * @param idVehiculoServicioOrdenTrabajo identificador del registro
     *                                       (PK de la tabla vehiculo_servicio_orden_trabajo).
     * @return el registro encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    VehiculoServicioOrdenTrabajo obtenerPorId(Integer idVehiculoServicioOrdenTrabajo);

    /**
     * Crea y persiste un nuevo registro vehículo–servicio–orden en la base de datos.
     *
     * @param entidad entidad {@link VehiculoServicioOrdenTrabajo} con la información a guardar
     *                (vehiculo, servicio, ordenTrabajo, fechaDeEjecucion).
     * @return el registro guardado con su id generado.
     */
    VehiculoServicioOrdenTrabajo crear(VehiculoServicioOrdenTrabajo entidad);

    /**
     * Actualiza los datos de un registro vehículo–servicio–orden existente.
     *
     * @param idVehiculoServicioOrdenTrabajo identificador del registro a actualizar.
     * @param entidadActualizada             objeto {@link VehiculoServicioOrdenTrabajo} con los nuevos datos
     *                                       (vehiculo, servicio, ordenTrabajo, fechaDeEjecucion).
     * @return el registro actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    VehiculoServicioOrdenTrabajo actualizar(Integer idVehiculoServicioOrdenTrabajo,
                                            VehiculoServicioOrdenTrabajo entidadActualizada);

    /**
     * Elimina un registro vehículo–servicio–orden por su identificador.
     *
     * @param idVehiculoServicioOrdenTrabajo identificador del registro a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un registro con ese id.
     */
    void eliminar(Integer idVehiculoServicioOrdenTrabajo);

    // ===== Métodos que envuelven las consultas del Repository =====

    /**
     * Lista todos los registros asociados a un vehículo específico.
     *
     * @param idVehiculo identificador del vehículo (placa).
     * @return lista de registros vehículo–servicio–orden para ese vehículo.
     */
    List<VehiculoServicioOrdenTrabajo> listarPorIdVehiculo(String idVehiculo);

    /**
     * Lista todos los registros asociados a un servicio específico.
     *
     * @param idServicio identificador del servicio.
     * @return lista de registros que usan ese servicio.
     */
    List<VehiculoServicioOrdenTrabajo> listarPorIdServicio(Integer idServicio);

    /**
     * Lista todos los registros asociados a una orden de trabajo específica.
     *
     * @param idOrdenTrabajo identificador de la orden de trabajo.
     * @return lista de registros vehículo–servicio–orden de esa orden.
     */
    List<VehiculoServicioOrdenTrabajo> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo);

    /**
     * Obtiene los registros cuya fecha_de_ejecucion esté dentro de un rango.
     *
     * @param inicio fecha de inicio (inclusive).
     * @param fin    fecha de fin (inclusive).
     * @return lista de registros ejecutados en ese rango de fechas.
     */
    List<VehiculoServicioOrdenTrabajo> buscarPorRangoFechaEjecucion(LocalDate inicio, LocalDate fin);

    /**
     * Cuenta cuántas ejecuciones tiene cada servicio.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idServicio (Integer o Number)</li>
     *     <li>[1] → nombreServicio (String)</li>
     *     <li>[2] → cantidadEjecuciones (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idServicio, nombreServicio, cantidadEjecuciones].
     */
    List<Object[]> contarEjecucionesPorServicio();

    /**
     * Obtiene la cantidad de servicios realizados por vehículo en un rango de fechas
     * de ejecución (fecha_de_ejecucion).
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idVehiculo (String)</li>
     *     <li>[1] → cantidadServicios (Long o Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idVehiculo, cantidadServicios].
     */
    List<Object[]> obtenerCantidadServiciosPorVehiculoEnRangoFechas(LocalDate inicio, LocalDate fin);

    /**
     * Calcula los servicios realizados por cliente en un rango de fechas de ejecución,
     * uniendo clientes → vehiculos → vehiculo_servicio_orden_trabajo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → cantidadServicios (Long o Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con [idCliente, nombreCliente, cantidadServicios].
     */
    List<Object[]> obtenerServiciosPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin);
}
