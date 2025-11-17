package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Especialidad;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.EspecialidadRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EspecialidadService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    // Inyección por constructor (recomendada)
    public EspecialidadServiceImpl(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Especialidad obtenerPorId(Integer idEspecialidad) {
        return especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró una especialidad con id: " + idEspecialidad
                ));
    }

    @Override
    public Especialidad crear(Especialidad especialidad) {
        // idEspecialidad es autogenerado (IDENTITY), no se debe asignar manualmente
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Especialidad actualizar(Integer idEspecialidad, Especialidad especialidadActualizada) {
        Especialidad existente = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe una especialidad con id: " + idEspecialidad
                ));

        // Solo actualizamos los campos que el usuario puede cambiar
        existente.setNombreEspecialidad(especialidadActualizada.getNombreEspecialidad());
        existente.setDescripcion(especialidadActualizada.getDescripcion());

        return especialidadRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idEspecialidad) {
        Especialidad existente = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe una especialidad con id: " + idEspecialidad
                ));

        especialidadRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Especialidad obtenerPorNombre(String nombreEspecialidad) {
        return especialidadRepository.findByNombreEspecialidad(nombreEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> buscarPorNombreConteniendo(String texto) {
        return especialidadRepository.findByNombreEspecialidadContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return especialidadRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> listarOrdenadasPorNombre() {
        return especialidadRepository.findAllByOrderByNombreEspecialidadAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarMecanicosPorEspecialidad() {
        return especialidadRepository.contarMecanicosPorEspecialidad();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> listarEspecialidadesSinMecanicos() {
        return especialidadRepository.findEspecialidadesSinMecanicos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerCostoTotalManoObraPorEspecialidadEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return especialidadRepository.costoTotalManoObraPorEspecialidadEnRangoFechas(inicio, fin);
    }
}
