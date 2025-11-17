package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoFactura;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.EstadoFacturaRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EstadoFacturaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EstadoFacturaServiceImpl implements EstadoFacturaService {

    private final EstadoFacturaRepository estadoFacturaRepository;

    // Inyección por constructor (recomendada)
    public EstadoFacturaServiceImpl(EstadoFacturaRepository estadoFacturaRepository) {
        this.estadoFacturaRepository = estadoFacturaRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<EstadoFactura> listarTodos() {
        return estadoFacturaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoFactura obtenerPorId(Byte idEstadoFactura) {
        return estadoFacturaRepository.findById(idEstadoFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un estado de factura con id: " + idEstadoFactura
                ));
    }

    @Override
    public EstadoFactura crear(EstadoFactura estadoFactura) {
        // idEstadoFactura es autogenerado (IDENTITY), no se debe asignar manualmente
        return estadoFacturaRepository.save(estadoFactura);
    }

    @Override
    public EstadoFactura actualizar(Byte idEstadoFactura, EstadoFactura estadoFacturaActualizado) {
        EstadoFactura existente = estadoFacturaRepository.findById(idEstadoFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un estado de factura con id: " + idEstadoFactura
                ));

        // Actualizamos solo los campos modificables
        existente.setEstado(estadoFacturaActualizado.getEstado());

        return estadoFacturaRepository.save(existente);
    }

    @Override
    public void eliminar(Byte idEstadoFactura) {
        EstadoFactura existente = estadoFacturaRepository.findById(idEstadoFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un estado de factura con id: " + idEstadoFactura
                ));

        estadoFacturaRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public EstadoFactura obtenerPorNombre(String estado) {
        return estadoFacturaRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoFactura> buscarPorNombreConteniendo(String texto) {
        return estadoFacturaRepository.findByEstadoContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoFactura> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return estadoFacturaRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoFactura> listarOrdenadosPorNombre() {
        return estadoFacturaRepository.findAllByOrderByEstadoAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarFacturasPorEstado() {
        return estadoFacturaRepository.contarFacturasPorEstado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoFactura> listarEstadosSinFacturas() {
        return estadoFacturaRepository.findEstadosSinFacturas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerTotalFacturadoPorEstadoEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return estadoFacturaRepository.totalFacturadoPorEstadoEnRangoFechas(inicio, fin);
    }
}
