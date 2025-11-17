package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Vehiculo;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoService {

    /**
     * Obtiene la lista completa de vehículos registrados en el sistema.
     *
     * @return lista de {@link Vehiculo}
     */
    List<Vehiculo> listarTodos();

    /**
     * Busca un vehículo por su identificador (placa).
     *
     * @param idVehiculo identificador del vehículo (PK de la tabla vehiculos).
     * @return el {@link Vehiculo} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un vehículo con esa placa.
     */
    Vehiculo obtenerPorId(String idVehiculo);

    /**
     * Crea y persiste un nuevo vehículo en la base de datos.
     * <p>
     * Nota: el idVehiculo (placa) no es autogenerado, debe venir informado en la entidad.
     *
     * @param vehiculo entidad {@link Vehiculo} con la información a guardar.
     * @return el vehículo guardado.
     */
    Vehiculo crear(Vehiculo vehiculo);

    /**
     * Actualiza los datos de un vehículo existente.
     *
     * @param idVehiculo        identificador del vehículo a actualizar (placa).
     * @param vehiculoActualizado objeto {@link Vehiculo} con los nuevos datos
     *                            (se usan tipoVehiculo, marca, modelo, anio y cliente).
     * @return el vehículo actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un vehículo con esa placa.
     */
    Vehiculo actualizar(String idVehiculo, Vehiculo vehiculoActualizado);

    /**
     * Elimina un vehículo por su identificador (placa).
     *
     * @param idVehiculo identificador del vehículo a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un vehículo con esa placa.
     */
    void eliminar(String idVehiculo);

    // ===== Métodos que envuelven las consultas del VehiculoRepository =====

    /**
     * Lista todos los vehículos asociados a un cliente específico.
     *
     * @param idCliente identificador del cliente (DNI, PK de la tabla clientes).
     * @return lista de vehículos pertenecientes a ese cliente.
     */
    List<Vehiculo> listarPorIdCliente(String idCliente);

    /**
     * Busca vehículos cuya marca contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param marca texto a buscar dentro de la marca.
     * @return lista de vehículos cuya marca contiene el texto dado.
     */
    List<Vehiculo> buscarPorMarcaConteniendo(String marca);

    /**
     * Busca vehículos cuyo modelo contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param modelo texto a buscar dentro del modelo.
     * @return lista de vehículos cuyo modelo contiene el texto dado.
     */
    List<Vehiculo> buscarPorModeloConteniendo(String modelo);

    /**
     * Busca vehículos cuyo año esté dentro del rango indicado.
     *
     * @param anioInicio año inicial (inclusive).
     * @param anioFin    año final (inclusive).
     * @return lista de vehículos cuyo campo anio está en ese rango.
     */
    List<Vehiculo> buscarPorRangoAnio(Short anioInicio, Short anioFin);

    /**
     * Cuenta cuántas órdenes de trabajo tiene cada vehículo.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idVehiculo (String)</li>
     *     <li>[1] → marca (String)</li>
     *     <li>[2] → modelo (String)</li>
     *     <li>[3] → anio (Short o Number)</li>
     *     <li>[4] → cantidadOrdenes (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idVehiculo, marca, modelo, anio, cantidadOrdenes].
     */
    List<Object[]> contarOrdenesPorVehiculo();

    /**
     * Obtiene los vehículos que nunca han tenido una orden de trabajo asociada
     * (no tienen registros en la tabla orden_trabajo).
     *
     * @return lista de vehículos sin órdenes.
     */
    List<Vehiculo> listarVehiculosSinOrdenes();

    /**
     * Calcula el total facturado por vehículo en un rango de fechas de facturas,
     * usando el recorrido vehiculos → orden_trabajo → facturas.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idVehiculo (String)</li>
     *     <li>[1] → marca (String)</li>
     *     <li>[2] → modelo (String)</li>
     *     <li>[3] → anio (Short o Number)</li>
     *     <li>[4] → totalFacturado (BigDecimal/Double)</li>
     *     <li>[5] → cantidadFacturas (Long o Number)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con
     * [idVehiculo, marca, modelo, anio, totalFacturado, cantidadFacturas].
     */
    List<Object[]> obtenerTotalFacturadoPorVehiculoEnRangoFechas(LocalDate inicio, LocalDate fin);
}
