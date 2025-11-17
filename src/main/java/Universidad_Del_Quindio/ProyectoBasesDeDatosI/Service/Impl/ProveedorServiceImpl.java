package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Proveedor;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.ProveedorRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ProveedorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    // Inyección por constructor (recomendada)
    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerPorId(Integer idProveedor) {
        return proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un proveedor con id: " + idProveedor
                ));
    }

    @Override
    public Proveedor crear(Proveedor proveedor) {
        // idProveedor es autogenerado (IDENTITY), no se debe asignar manualmente
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Integer idProveedor, Proveedor proveedorActualizado) {
        Proveedor existente = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un proveedor con id: " + idProveedor
                ));

        // Actualizamos solo los campos modificables
        existente.setNombre(proveedorActualizado.getNombre());
        existente.setTelefono(proveedorActualizado.getTelefono());
        existente.setDireccion(proveedorActualizado.getDireccion());

        return proveedorRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idProveedor) {
        Proveedor existente = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un proveedor con id: " + idProveedor
                ));

        proveedorRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombreConteniendo(String texto) {
        return proveedorRepository.findByNombreContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorDireccionConteniendo(String texto) {
        return proveedorRepository.findByDireccionContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return proveedorRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenSuministrosPorProveedor() {
        return proveedorRepository.resumenSuministrosPorProveedor();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedoresSinSuministros() {
        return proveedorRepository.findProveedoresSinSuministros();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerCostoTotalPorProveedorEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return proveedorRepository.costoTotalPorProveedorEnRangoFechas(inicio, fin);
    }
}
