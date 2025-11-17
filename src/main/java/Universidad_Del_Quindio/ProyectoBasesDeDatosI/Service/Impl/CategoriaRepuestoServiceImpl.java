package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.CategoriaRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.CategoriaRepuestoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.CategoriaRepuestoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CategoriaRepuestoServiceImpl implements CategoriaRepuestoService {

    private final CategoriaRepuestoRepository categoriaRepuestoRepository;

    // Inyección por constructor (recomendada)
    public CategoriaRepuestoServiceImpl(CategoriaRepuestoRepository categoriaRepuestoRepository) {
        this.categoriaRepuestoRepository = categoriaRepuestoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepuesto> listarTodas() {
        return categoriaRepuestoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaRepuesto obtenerPorId(Integer idCategoria) {
        return categoriaRepuestoRepository.findById(idCategoria)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró una categoría de repuesto con id: " + idCategoria
                ));
    }

    @Override
    public CategoriaRepuesto crear(CategoriaRepuesto categoriaRepuesto) {
        // idCategoria es autogenerado (IDENTITY), no se debe asignar manualmente
        return categoriaRepuestoRepository.save(categoriaRepuesto);
    }

    @Override
    public CategoriaRepuesto actualizar(Integer idCategoria, CategoriaRepuesto categoriaRepuestoActualizada) {
        CategoriaRepuesto existente = categoriaRepuestoRepository.findById(idCategoria)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe una categoría de repuesto con id: " + idCategoria
                ));

        // Actualizamos solo los campos modificables
        existente.setCategoria(categoriaRepuestoActualizada.getCategoria());

        return categoriaRepuestoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idCategoria) {
        CategoriaRepuesto existente = categoriaRepuestoRepository.findById(idCategoria)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe una categoría de repuesto con id: " + idCategoria
                ));

        categoriaRepuestoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public CategoriaRepuesto obtenerPorNombre(String nombreCategoria) {
        return categoriaRepuestoRepository.findByCategoria(nombreCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepuesto> buscarPorNombreConteniendo(String texto) {
        return categoriaRepuestoRepository.findByCategoriaContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return categoriaRepuestoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepuesto> listarOrdenadasPorNombre() {
        return categoriaRepuestoRepository.findAllByOrderByCategoriaAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarRepuestosPorCategoria() {
        return categoriaRepuestoRepository.contarRepuestosPorCategoria();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepuesto> listarCategoriasSinRepuestos() {
        return categoriaRepuestoRepository.findCategoriasSinRepuestos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerStockYValorTotalPorCategoriaEnBodega() {
        return categoriaRepuestoRepository.stockYValorTotalPorCategoriaEnBodega();
    }
}