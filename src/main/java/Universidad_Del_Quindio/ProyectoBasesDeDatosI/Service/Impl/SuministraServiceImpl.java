package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Suministra;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.SuministraId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.SuministraRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.SuministraService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SuministraServiceImpl implements SuministraService {

    private final SuministraRepository suministraRepository;

    // Inyección por constructor (recomendada)
    public SuministraServiceImpl(SuministraRepository suministraRepository) {
        this.suministraRepository = suministraRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> listarTodos() {
        return suministraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Suministra obtenerPorId(SuministraId id) {
        return suministraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un registro de suministro con id (proveedor="
                                + id.getIdProveedor() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));
    }

    @Override
    public Suministra crear(Suministra suministra) {
        // Si el ID compuesto aún no está seteado, lo construimos desde las relaciones
        if (suministra.getId() == null
                && suministra.getProveedor() != null
                && suministra.getRepuesto() != null) {

            SuministraId id = new SuministraId(
                    suministra.getProveedor().getIdProveedor(),
                    suministra.getRepuesto().getIdRepuesto()
            );
            suministra.setId(id);
        }
        return suministraRepository.save(suministra);
    }

    @Override
    public Suministra actualizar(SuministraId id, Suministra suministraActualizada) {
        Suministra existente = suministraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un registro de suministro con id (proveedor="
                                + id.getIdProveedor() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        // Normalmente no se cambian las claves (proveedor / repuesto) en una PK compuesta.
        // Actualizamos solo los campos de datos.
        existente.setCostoUnitario(suministraActualizada.getCostoUnitario());
        existente.setCantidad(suministraActualizada.getCantidad());
        existente.setCostoTotal(suministraActualizada.getCostoTotal());
        existente.setFechaIngreso(suministraActualizada.getFechaIngreso());

        return suministraRepository.save(existente);
    }

    @Override
    public void eliminar(SuministraId id) {
        Suministra existente = suministraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un registro de suministro con id (proveedor="
                                + id.getIdProveedor() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        suministraRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> listarPorIdProveedor(Integer idProveedor) {
        return suministraRepository.findByProveedor_IdProveedor(idProveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> listarPorIdRepuesto(Integer idRepuesto) {
        return suministraRepository.findByRepuesto_IdRepuesto(idRepuesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> buscarPorRangoFechaIngreso(LocalDate inicio, LocalDate fin) {
        return suministraRepository.findByFechaIngresoBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> buscarPorRangoCostoUnitario(BigDecimal costoMin, BigDecimal costoMax) {
        return suministraRepository.findByCostoUnitarioBetween(costoMin, costoMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> buscarPorCantidadMayorIgual(Integer cantidadMinima) {
        return suministraRepository.findByCantidadGreaterThanEqual(cantidadMinima);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suministra> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return suministraRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenComprasPorProveedor() {
        return suministraRepository.resumenComprasPorProveedor();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenComprasPorRepuesto() {
        return suministraRepository.resumenComprasPorRepuesto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerComprasPorProveedorEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return suministraRepository.comprasPorProveedorEnRangoFechas(inicio, fin);
    }
}
