package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanicoId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.OrdenTrabajoMecanicoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.OrdenTrabajoMecanicoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrdenTrabajoMecanicoServiceImpl implements OrdenTrabajoMecanicoService {

    private final OrdenTrabajoMecanicoRepository ordenTrabajoMecanicoRepository;

    // Inyección por constructor (recomendada)
    public OrdenTrabajoMecanicoServiceImpl(OrdenTrabajoMecanicoRepository ordenTrabajoMecanicoRepository) {
        this.ordenTrabajoMecanicoRepository = ordenTrabajoMecanicoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> listarTodos() {
        return ordenTrabajoMecanicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTrabajoMecanico obtenerPorId(OrdenTrabajoMecanicoId id) {
        return ordenTrabajoMecanicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un registro orden_trabajo_mecanico con id (orden="
                                + id.getIdOrdenTrabajo() + ", mecánico=" + id.getIdMecanico() + ")"
                ));
    }

    @Override
    public OrdenTrabajoMecanico crear(OrdenTrabajoMecanico entidad) {
        // Si el ID compuesto aún no está seteado, lo construimos desde las relaciones
        if (entidad.getId() == null
                && entidad.getOrdenTrabajo() != null
                && entidad.getMecanico() != null) {

            OrdenTrabajoMecanicoId id = new OrdenTrabajoMecanicoId(
                    entidad.getOrdenTrabajo().getIdOrdenTrabajo(),
                    entidad.getMecanico().getIdMecanico()
            );
            entidad.setId(id);
        }
        return ordenTrabajoMecanicoRepository.save(entidad);
    }

    @Override
    public OrdenTrabajoMecanico actualizar(OrdenTrabajoMecanicoId id, OrdenTrabajoMecanico entidadActualizada) {
        OrdenTrabajoMecanico existente = ordenTrabajoMecanicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un registro orden_trabajo_mecanico con id (orden="
                                + id.getIdOrdenTrabajo() + ", mecánico=" + id.getIdMecanico() + ")"
                ));

        // Normalmente no se cambian las claves (orden / mecánico) en una PK compuesta.
        // Actualizamos solo los campos de datos (y rol si quieres permitirlo).
        existente.setRol(entidadActualizada.getRol());
        existente.setHoras(entidadActualizada.getHoras());
        existente.setCostoHora(entidadActualizada.getCostoHora());
        existente.setCostoTotal(entidadActualizada.getCostoTotal());

        return ordenTrabajoMecanicoRepository.save(existente);
    }

    @Override
    public void eliminar(OrdenTrabajoMecanicoId id) {
        OrdenTrabajoMecanico existente = ordenTrabajoMecanicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un registro orden_trabajo_mecanico con id (orden="
                                + id.getIdOrdenTrabajo() + ", mecánico=" + id.getIdMecanico() + ")"
                ));

        ordenTrabajoMecanicoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo) {
        return ordenTrabajoMecanicoRepository.findByOrdenTrabajo_IdOrdenTrabajo(idOrdenTrabajo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> listarPorIdMecanico(String idMecanico) {
        return ordenTrabajoMecanicoRepository.findByMecanico_IdMecanico(idMecanico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> listarPorIdRol(Byte idRol) {
        return ordenTrabajoMecanicoRepository.findByRol_IdRol(idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> buscarPorRangoHoras(BigDecimal horasMin, BigDecimal horasMax) {
        return ordenTrabajoMecanicoRepository.findByHorasBetween(horasMin, horasMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajoMecanico> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return ordenTrabajoMecanicoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ordenTrabajoMecanicoRepository.horasYCostoTotalPorMecanicoEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasYCostoTotalPorOrden() {
        return ordenTrabajoMecanicoRepository.horasYCostoTotalPorOrden();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasYCostoTotalPorRolEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ordenTrabajoMecanicoRepository.horasYCostoTotalPorRolEnRangoFechas(inicio, fin);
    }
}

