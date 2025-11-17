package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Repuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.RepuestoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.RepuestoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    // Inyección por constructor (recomendada)
    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Repuesto> listarTodos() {
        return repuestoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Repuesto obtenerPorId(Integer idRepuesto) {
        return repuestoRepository.findById(idRepuesto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un repuesto con id: " + idRepuesto
                ));
    }

    @Override
    public Repuesto crear(Repuesto repuesto) {
        // idRepuesto es autogenerado (IDENTITY), no se debe asignar manualmente
        return repuestoRepository.save(repuesto);
    }

    @Override
    public Repuesto actualizar(Integer idRepuesto, Repuesto repuestoActualizado) {
        Repuesto existente = repuestoRepository.findById(idRepuesto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un repuesto con id: " + idRepuesto
                ));

        // Actualizamos solo los campos modificables
        existente.setNombre(repuestoActualizado.getNombre());
        existente.setDescripcion(repuestoActualizado.getDescripcion());
        existente.setCategoria(repuestoActualizado.getCategoria());

        return repuestoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idRepuesto) {
        Repuesto existente = repuestoRepository.findById(idRepuesto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un repuesto con id: " + idRepuesto
                ));

        repuestoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Repuesto obtenerPorNombre(String nombre) {
        return repuestoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repuesto> buscarPorNombreConteniendo(String texto) {
        return repuestoRepository.findByNombreContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repuesto> listarPorIdCategoria(Integer idCategoria) {
        return repuestoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return repuestoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerStockYValorTotalPorRepuesto() {
        return repuestoRepository.stockYValorTotalPorRepuesto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerVentasPorRepuestoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return repuestoRepository.ventasPorRepuestoEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerComprasPorRepuestoEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return repuestoRepository.comprasPorRepuestoEnRangoFechas(inicio, fin);
    }
}
