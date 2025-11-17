package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoOrden;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.EstadoOrdenRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EstadoOrdenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EstadoOrdenServiceImpl implements EstadoOrdenService {

    private final EstadoOrdenRepository estadoOrdenRepository;

    // Inyección por constructor (recomendada)
    public EstadoOrdenServiceImpl(EstadoOrdenRepository estadoOrdenRepository) {
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<EstadoOrden> listarTodos() {
        return estadoOrdenRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoOrden obtenerPorId(Byte idEstadoOrden) {
        return estadoOrdenRepository.findById(idEstadoOrden)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un estado de orden con id: " + idEstadoOrden
                ));
    }

    @Override
    public EstadoOrden crear(EstadoOrden estadoOrden) {
        // idEstadoOrden es autogenerado (IDENTITY), no se debe asignar manualmente
        return estadoOrdenRepository.save(estadoOrden);
    }

    @Override
    public EstadoOrden actualizar(Byte idEstadoOrden, EstadoOrden estadoOrdenActualizado) {
        EstadoOrden existente = estadoOrdenRepository.findById(idEstadoOrden)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un estado de orden con id: " + idEstadoOrden
                ));

        // Actualizamos solo los campos modificables
        existente.setEstado(estadoOrdenActualizado.getEstado());

        return estadoOrdenRepository.save(existente);
    }

    @Override
    public void eliminar(Byte idEstadoOrden) {
        EstadoOrden existente = estadoOrdenRepository.findById(idEstadoOrden)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un estado de orden con id: " + idEstadoOrden
                ));

        estadoOrdenRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public EstadoOrden obtenerPorNombre(String estado) {
        return estadoOrdenRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoOrden> buscarPorNombreConteniendo(String texto) {
        return estadoOrdenRepository.findByEstadoContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoOrden> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return estadoOrdenRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoOrden> listarOrdenadosPorNombre() {
        return estadoOrdenRepository.findAllByOrderByEstadoAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarOrdenesPorEstado() {
        return estadoOrdenRepository.contarOrdenesPorEstado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoOrden> listarEstadosSinOrdenes() {
        return estadoOrdenRepository.findEstadosSinOrdenes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerPromedioDiasPorEstado() {
        return estadoOrdenRepository.promedioDiasPorEstado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerPromedioDiasPorEstadoEnRango(LocalDate inicio, LocalDate fin) {
        // El repository espera java.sql.Date, así que convertimos desde LocalDate
        Date inicioSql = Date.valueOf(inicio);
        Date finSql = Date.valueOf(fin);
        return estadoOrdenRepository.promedioDiasPorEstadoEnRango(inicioSql, finSql);
    }
}
