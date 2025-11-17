package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Mecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.MecanicoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.MecanicoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MecanicoServiceImpl implements MecanicoService {

    private final MecanicoRepository mecanicoRepository;

    // Inyección por constructor (recomendada)
    public MecanicoServiceImpl(MecanicoRepository mecanicoRepository) {
        this.mecanicoRepository = mecanicoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> listarTodos() {
        return mecanicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mecanico obtenerPorId(String idMecanico) {
        return mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un mecánico con id: " + idMecanico
                ));
    }

    @Override
    public Mecanico crear(Mecanico mecanico) {
        // idMecanico NO es autogenerado, debe venir asignado antes de guardar
        return mecanicoRepository.save(mecanico);
    }

    @Override
    public Mecanico actualizar(String idMecanico, Mecanico mecanicoActualizado) {
        Mecanico existente = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un mecánico con id: " + idMecanico
                ));

        // Actualizamos solo los campos modificables
        existente.setExperiencia(mecanicoActualizado.getExperiencia());
        existente.setEspecialidad(mecanicoActualizado.getEspecialidad());
        existente.setCostoHora(mecanicoActualizado.getCostoHora());

        return mecanicoRepository.save(existente);
    }

    @Override
    public void eliminar(String idMecanico) {
        Mecanico existente = mecanicoRepository.findById(idMecanico)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un mecánico con id: " + idMecanico
                ));

        mecanicoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> listarPorIdEspecialidad(Integer idEspecialidad) {
        return mecanicoRepository.findByEspecialidad_IdEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> buscarPorExperienciaMayorIgual(Byte experiencia) {
        return mecanicoRepository.findByExperienciaGreaterThanEqual(experiencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> buscarPorRangoCostoHora(BigDecimal costoMin, BigDecimal costoMax) {
        return mecanicoRepository.findByCostoHoraBetween(costoMin, costoMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return mecanicoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return mecanicoRepository.horasYCostoTotalPorMecanicoEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> listarMecanicosSinOrdenes() {
        return mecanicoRepository.findMecanicosSinOrdenes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasPorMecanicoYAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return mecanicoRepository.horasPorMecanicoYAreaEnRangoFechas(inicio, fin);
    }
}
