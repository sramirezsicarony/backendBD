package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Bodega;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.BodegaId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.BodegaRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.BodegaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;

    // Inyección por constructor (recomendada)
    public BodegaServiceImpl(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> listarTodos() {
        return bodegaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Bodega obtenerPorId(BodegaId id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un registro de bodega con id (almacén="
                                + id.getIdAlmacen() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));
    }

    @Override
    public Bodega crear(Bodega bodega) {
        // Si el ID compuesto aún no está seteado, lo construimos desde las relaciones
        if (bodega.getId() == null && bodega.getAlmacen() != null && bodega.getRepuesto() != null) {
            BodegaId id = new BodegaId(
                    bodega.getAlmacen().getIdAlmacen(),
                    bodega.getRepuesto().getIdRepuesto()
            );
            bodega.setId(id);
        }
        return bodegaRepository.save(bodega);
    }

    @Override
    public Bodega actualizar(BodegaId id, Bodega bodegaActualizada) {
        Bodega existente = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un registro de bodega con id (almacén="
                                + id.getIdAlmacen() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        // Normalmente no se cambian las claves (almacén / repuesto) en una PK compuesta.
        // Actualizamos solo los campos de datos.
        existente.setStock(bodegaActualizada.getStock());
        existente.setPrecioVenta(bodegaActualizada.getPrecioVenta());

        return bodegaRepository.save(existente);
    }

    @Override
    public void eliminar(BodegaId id) {
        Bodega existente = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un registro de bodega con id (almacén="
                                + id.getIdAlmacen() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        bodegaRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> listarPorIdAlmacen(Integer idAlmacen) {
        return bodegaRepository.findByAlmacen_IdAlmacen(idAlmacen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> buscarPorStockMayorIgual(Integer stockMinimo) {
        return bodegaRepository.findByStockGreaterThanEqual(stockMinimo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> buscarPorRangoPrecioVenta(BigDecimal precioMin, BigDecimal precioMax) {
        return bodegaRepository.findByPrecioVentaBetween(precioMin, precioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bodega> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return bodegaRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerStockYValorTotalPorAlmacen() {
        return bodegaRepository.stockYValorTotalPorAlmacen();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerStockYValorTotalPorRepuestoGlobal() {
        return bodegaRepository.stockYValorTotalPorRepuestoGlobal();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerRepuestosConStockGlobalBajo(Integer umbral) {
        return bodegaRepository.repuestosConStockGlobalBajo(umbral);
    }
}
