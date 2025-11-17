package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajos;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajosId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.AreaTrabajosRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AreaTrabajosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AreaTrabajosServiceImpl implements AreaTrabajosService {

    private final AreaTrabajosRepository areaTrabajosRepository;

    // Inyección por constructor (recomendada)
    public AreaTrabajosServiceImpl(AreaTrabajosRepository areaTrabajosRepository) {
        this.areaTrabajosRepository = areaTrabajosRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<AreaTrabajos> listarTodos() {
        return areaTrabajosRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AreaTrabajos obtenerPorId(AreaTrabajosId id) {
        return areaTrabajosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró una asignación área–mecánico con id (area="
                                + id.getIdAreaLaboral() + ", mecánico=" + id.getIdMecanico() + ")"
                ));
    }

    @Override
    public AreaTrabajos crear(AreaTrabajos entidad) {
        // Si el ID compuesto aún no está seteado, lo construimos desde las relaciones
        if (entidad.getId() == null
                && entidad.getAreaLaboral() != null
                && entidad.getMecanico() != null) {

            AreaTrabajosId id = new AreaTrabajosId(
                    entidad.getAreaLaboral().getIdAreaLaboral(),
                    entidad.getMecanico().getIdMecanico()
            );
            entidad.setId(id);
        }
        return areaTrabajosRepository.save(entidad);
    }

    @Override
    public AreaTrabajos actualizar(AreaTrabajosId id, AreaTrabajos entidadActualizada) {
        AreaTrabajos existente = areaTrabajosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe una asignación área–mecánico con id (area="
                                + id.getIdAreaLaboral() + ", mecánico=" + id.getIdMecanico() + ")"
                ));

        // IMPORTANTE:
        // En una relación con PK compuesta (área + mecánico) NO es buena práctica
        // cambiar las claves. Si necesitas cambiar área o mecánico, elimina el
        // registro y crea uno nuevo.
        //
        // Aquí no hay otros campos de datos (solo relaciones y timestamps),
        // por lo que no realizamos cambios sobre el objeto existente.
        // Si en el futuro agregas más columnas (ej: fechaAsignacion), podrías
        // actualizarlas aquí.

        return areaTrabajosRepository.save(existente);
    }

    @Override
    public void eliminar(AreaTrabajosId id) {
        AreaTrabajos existente = areaTrabajosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe una asignación área–mecánico con id (area="
                                + id.getIdAreaLaboral() + ", mecánico=" + id.getIdMecanico() + ")"
                ));

        areaTrabajosRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<AreaTrabajos> listarPorIdAreaLaboral(Integer idAreaLaboral) {
        return areaTrabajosRepository.findByAreaLaboral_IdAreaLaboral(idAreaLaboral);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaTrabajos> listarPorIdMecanico(String idMecanico) {
        return areaTrabajosRepository.findByMecanico_IdMecanico(idMecanico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaTrabajos> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return areaTrabajosRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaTrabajos> buscarPorNombreAreaConteniendo(String texto) {
        return areaTrabajosRepository.findByAreaLaboral_AreaContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarMecanicosPorAreaLaboral() {
        return areaTrabajosRepository.contarMecanicosPorAreaLaboral();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarAreasPorMecanico() {
        return areaTrabajosRepository.contarAreasPorMecanico();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasYCostoPorAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return areaTrabajosRepository.horasYCostoPorAreaEnRangoFechas(inicio, fin);
    }
}
