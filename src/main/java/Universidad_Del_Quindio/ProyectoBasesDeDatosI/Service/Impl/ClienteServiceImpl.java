package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Cliente;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.ClienteRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    // Inyección por constructor (recomendada)
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerPorId(String idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un cliente con id: " + idCliente
                ));
    }

    @Override
    public Cliente crear(Cliente cliente) {
        // idCliente NO es autogenerado, debe venir asignado antes de guardar
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(String idCliente, Cliente clienteActualizado) {
        Cliente existente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un cliente con id: " + idCliente
                ));

        // Actualizamos solo los campos modificables
        existente.setNombre(clienteActualizado.getNombre());
        existente.setTelefono(clienteActualizado.getTelefono());
        existente.setCorreo(clienteActualizado.getCorreo());

        return clienteRepository.save(existente);
    }

    @Override
    public void eliminar(String idCliente) {
        Cliente existente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un cliente con id: " + idCliente
                ));

        clienteRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombreConteniendo(String texto) {
        return clienteRepository.findByNombreContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorTelefonoConPrefijo(String prefijo) {
        return clienteRepository.findByTelefonoStartingWith(prefijo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return clienteRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarVehiculosPorCliente() {
        return clienteRepository.contarVehiculosPorCliente();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientesSinVehiculos() {
        return clienteRepository.findClientesSinVehiculos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return clienteRepository.resumenFacturacionPorClienteEnRangoFechas(inicio, fin);
    }
}
