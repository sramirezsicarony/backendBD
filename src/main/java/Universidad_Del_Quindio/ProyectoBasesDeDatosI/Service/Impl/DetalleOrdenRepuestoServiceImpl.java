package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuestoId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.DetalleOrdenRepuestoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.DetalleOrdenRepuestoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DetalleOrdenRepuestoServiceImpl implements DetalleOrdenRepuestoService {

    private final DetalleOrdenRepuestoRepository detalleOrdenRepuestoRepository;

    // Inyección por constructor (recomendada)
    public DetalleOrdenRepuestoServiceImpl(DetalleOrdenRepuestoRepository detalleOrdenRepuestoRepository) {
        this.detalleOrdenRepuestoRepository = detalleOrdenRepuestoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenRepuesto> listarTodos() {
        return detalleOrdenRepuestoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleOrdenRepuesto obtenerPorId(DetalleOrdenRepuestoId id) {
        return detalleOrdenRepuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un detalle de repuesto con id (orden="
                                + id.getIdOrdenTrabajo() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));
    }

    @Override
    public DetalleOrdenRepuesto crear(DetalleOrdenRepuesto detalle) {
        // Si el ID compuesto aún no está seteado, lo construimos desde las relaciones
        if (detalle.getId() == null
                && detalle.getOrdenTrabajo() != null
                && detalle.getRepuesto() != null) {

            DetalleOrdenRepuestoId id = new DetalleOrdenRepuestoId(
                    detalle.getOrdenTrabajo().getIdOrdenTrabajo(),
                    detalle.getRepuesto().getIdRepuesto()
            );
            detalle.setId(id);
        }
        return detalleOrdenRepuestoRepository.save(detalle);
    }

    @Override
    public DetalleOrdenRepuesto actualizar(DetalleOrdenRepuestoId id, DetalleOrdenRepuesto detalleActualizado) {
        DetalleOrdenRepuesto existente = detalleOrdenRepuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un detalle de repuesto con id (orden="
                                + id.getIdOrdenTrabajo() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        // Normalmente no se cambian las claves (orden / repuesto) en una PK compuesta.
        // Actualizamos solo los campos de datos.
        existente.setCantidad(detalleActualizado.getCantidad());
        existente.setSubTotal(detalleActualizado.getSubTotal());

        return detalleOrdenRepuestoRepository.save(existente);
    }

    @Override
    public void eliminar(DetalleOrdenRepuestoId id) {
        DetalleOrdenRepuesto existente = detalleOrdenRepuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un detalle de repuesto con id (orden="
                                + id.getIdOrdenTrabajo() + ", repuesto=" + id.getIdRepuesto() + ")"
                ));

        detalleOrdenRepuestoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenRepuesto> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo) {
        return detalleOrdenRepuestoRepository.findByOrdenTrabajo_IdOrdenTrabajo(idOrdenTrabajo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenRepuesto> listarPorIdRepuesto(Integer idRepuesto) {
        return detalleOrdenRepuestoRepository.findByRepuesto_IdRepuesto(idRepuesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenRepuesto> buscarPorCantidadMayorIgual(Short cantidadMinima) {
        return detalleOrdenRepuestoRepository.findByCantidadGreaterThanEqual(cantidadMinima);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenRepuesto> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return detalleOrdenRepuestoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenRepuestosPorOrden() {
        return detalleOrdenRepuestoRepository.resumenRepuestosPorOrden();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerRepuestosMasUtilizados() {
        return detalleOrdenRepuestoRepository.repuestosMasUtilizados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerConsumoRepuestosPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return detalleOrdenRepuestoRepository.consumoRepuestosPorClienteEnRangoFechas(inicio, fin);
    }
}
