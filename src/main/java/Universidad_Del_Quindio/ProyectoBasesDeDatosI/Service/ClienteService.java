package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClienteService {

    /**
     * Obtiene la lista completa de clientes registrados en el sistema.
     *
     * @return lista de {@link Cliente}
     */
    List<Cliente> listarTodos();

    /**
     * Busca un cliente por su identificador (DNI).
     *
     * @param idCliente identificador del cliente (PK de la tabla clientes).
     * @return el {@link Cliente} encontrado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un cliente con ese id.
     */
    Cliente obtenerPorId(String idCliente);

    /**
     * Crea y persiste un nuevo cliente en la base de datos.
     * <p>
     * Nota: el idCliente no es autogenerado, debe venir informado en la entidad.
     *
     * @param cliente entidad {@link Cliente} con la información a guardar.
     * @return el cliente guardado.
     */
    Cliente crear(Cliente cliente);

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param idCliente          identificador del cliente a actualizar.
     * @param clienteActualizado objeto {@link Cliente} con los nuevos datos
     *                           (se usan nombre, teléfono y correo).
     * @return el cliente actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un cliente con ese id.
     */
    Cliente actualizar(String idCliente, Cliente clienteActualizado);

    /**
     * Elimina un cliente por su identificador.
     *
     * @param idCliente identificador del cliente a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no existe un cliente con ese id.
     */
    void eliminar(String idCliente);

    // ===== Métodos que envuelven las consultas del ClienteRepository =====

    /**
     * Busca un cliente por su correo exacto.
     *
     * @param correo correo electrónico del cliente (columna correo).
     * @return el cliente encontrado o null si no existe.
     */
    Cliente obtenerPorCorreo(String correo);

    /**
     * Busca clientes cuyo nombre contenga el texto indicado
     * (búsqueda LIKE, ignorando mayúsculas/minúsculas).
     *
     * @param texto texto a buscar dentro del nombre del cliente.
     * @return lista de clientes cuyo nombre contiene el texto dado.
     */
    List<Cliente> buscarPorNombreConteniendo(String texto);

    /**
     * Busca clientes cuyo teléfono comience por el prefijo indicado.
     * Útil para filtrar por indicativo o código de país.
     *
     * @param prefijo prefijo con el que debe comenzar el teléfono.
     * @return lista de clientes cuyo teléfono empieza por dicho prefijo.
     */
    List<Cliente> buscarPorTelefonoConPrefijo(String prefijo);

    /**
     * Obtiene los clientes creados dentro de un rango de fechas (usando created_at).
     *
     * @param inicio fecha/hora de inicio (inclusive).
     * @param fin    fecha/hora de fin (inclusive).
     * @return lista de clientes creados en ese rango.
     */
    List<Cliente> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Cuenta cuántos vehículos tiene registrado cada cliente.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → cantidadVehiculos (Long o Number)</li>
     * </ul>
     *
     * @return lista de registros con [idCliente, nombreCliente, cantidadVehiculos].
     */
    List<Object[]> contarVehiculosPorCliente();

    /**
     * Obtiene los clientes que no tienen ningún vehículo registrado
     * (no tienen registros en la tabla vehiculos).
     *
     * @return lista de clientes sin vehículos.
     */
    List<Cliente> listarClientesSinVehiculos();

    /**
     * Obtiene un resumen de facturación por cliente en un rango de fechas,
     * usando la columna fecha_creacion de la tabla facturas.
     * <p>
     * Cada posición del arreglo Object[] representa:
     * <ul>
     *     <li>[0] → idCliente (String)</li>
     *     <li>[1] → nombreCliente (String)</li>
     *     <li>[2] → totalFacturado (BigDecimal/Double)</li>
     *     <li>[3] → manoObraTotal (BigDecimal/Double)</li>
     *     <li>[4] → repuestosTotal (BigDecimal/Double)</li>
     *     <li>[5] → impuestoTotal (BigDecimal/Double)</li>
     * </ul>
     *
     * @param inicio fecha de inicio del rango (inclusive).
     * @param fin    fecha de fin del rango (inclusive).
     * @return lista de registros con
     * [idCliente, nombreCliente, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal].
     */
    List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin);
}
