package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Almacen;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.AlmacenRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AlmacenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository almacenRepository;

    // Inyección por constructor (recomendada)
    public AlmacenServiceImpl(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Almacen> listarTodos() {
        return almacenRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Almacen obtenerPorId(Integer idAlmacen) {
        return almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un almacén con id: " + idAlmacen
                ));
    }

    @Override
    public Almacen crear(Almacen almacen) {
        // idAlmacen es autogenerado (IDENTITY), no se debe asignar manualmente
        return almacenRepository.save(almacen);
    }

    @Override
    public Almacen actualizar(Integer idAlmacen, Almacen almacenActualizado) {
        Almacen existente = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un almacén con id: " + idAlmacen
                ));

        // Actualizamos solo los campos modificables
        existente.setNombre(almacenActualizado.getNombre());
        existente.setDireccion(almacenActualizado.getDireccion());

        return almacenRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idAlmacen) {
        Almacen existente = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un almacén con id: " + idAlmacen
                ));

        almacenRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Almacen obtenerPorNombre(String nombre) {
        return almacenRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Almacen> buscarPorNombreConteniendo(String texto) {
        return almacenRepository.findByNombreContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Almacen> buscarPorDireccionConteniendo(String texto) {
        return almacenRepository.findByDireccionContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Almacen> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return almacenRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenRepuestosYStockPorAlmacen() {
        return almacenRepository.resumenRepuestosYStockPorAlmacen();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Almacen> listarAlmacenesSinStock() {
        return almacenRepository.findAlmacenesSinStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerStockYValorTotalPorAlmacen() {
        return almacenRepository.stockYValorTotalPorAlmacen();
    }
}
