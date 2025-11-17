package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Vehiculo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.VehiculoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.VehiculoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    // Inyección por constructor (recomendada)
    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo obtenerPorId(String idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un vehículo con placa: " + idVehiculo
                ));
    }

    @Override
    public Vehiculo crear(Vehiculo vehiculo) {
        // idVehiculo (placa) NO es autogenerado, debe venir asignado antes de guardar
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public Vehiculo actualizar(String idVehiculo, Vehiculo vehiculoActualizado) {
        Vehiculo existente = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un vehículo con placa: " + idVehiculo
                ));

        // Actualizamos solo los campos modificables
        existente.setTipoVehiculo(vehiculoActualizado.getTipoVehiculo());
        existente.setMarca(vehiculoActualizado.getMarca());
        existente.setModelo(vehiculoActualizado.getModelo());
        existente.setAnio(vehiculoActualizado.getAnio());
        existente.setCliente(vehiculoActualizado.getCliente());

        return vehiculoRepository.save(existente);
    }

    @Override
    public void eliminar(String idVehiculo) {
        Vehiculo existente = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un vehículo con placa: " + idVehiculo
                ));

        vehiculoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarPorIdCliente(String idCliente) {
        return vehiculoRepository.findByCliente_IdCliente(idCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorMarcaConteniendo(String marca) {
        return vehiculoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorModeloConteniendo(String modelo) {
        return vehiculoRepository.findByModeloContainingIgnoreCase(modelo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> buscarPorRangoAnio(Short anioInicio, Short anioFin) {
        return vehiculoRepository.findByAnioBetween(anioInicio, anioFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarOrdenesPorVehiculo() {
        return vehiculoRepository.contarOrdenesPorVehiculo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculosSinOrdenes() {
        return vehiculoRepository.findVehiculosSinOrdenes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerTotalFacturadoPorVehiculoEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return vehiculoRepository.totalFacturadoPorVehiculoEnRangoFechas(inicio, fin);
    }
}
