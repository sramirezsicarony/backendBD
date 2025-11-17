package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaLaboral;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.AreaLaboralRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AreaLaboralService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AreaLaboralServiceImpl implements AreaLaboralService {

    private final AreaLaboralRepository areaLaboralRepository;

    // Inyección por constructor (recomendada)
    public AreaLaboralServiceImpl(AreaLaboralRepository areaLaboralRepository) {
        this.areaLaboralRepository = areaLaboralRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<AreaLaboral> listarTodas() {
        return areaLaboralRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AreaLaboral obtenerPorId(Integer idAreaLaboral) {
        return areaLaboralRepository.findById(idAreaLaboral)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un área laboral con id: " + idAreaLaboral
                ));
    }

    @Override
    public AreaLaboral crear(AreaLaboral areaLaboral) {
        // idAreaLaboral es autogenerado (IDENTITY), no se debe asignar manualmente
        return areaLaboralRepository.save(areaLaboral);
    }

    @Override
    public AreaLaboral actualizar(Integer idAreaLaboral, AreaLaboral areaLaboralActualizada) {
        AreaLaboral existente = areaLaboralRepository.findById(idAreaLaboral)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un área laboral con id: " + idAreaLaboral
                ));

        // Actualizamos solo los campos modificables
        existente.setArea(areaLaboralActualizada.getArea());

        return areaLaboralRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idAreaLaboral) {
        AreaLaboral existente = areaLaboralRepository.findById(idAreaLaboral)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un área laboral con id: " + idAreaLaboral
                ));

        areaLaboralRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public AreaLaboral obtenerPorNombre(String nombreArea) {
        return areaLaboralRepository.findByArea(nombreArea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaLaboral> buscarPorNombreConteniendo(String texto) {
        return areaLaboralRepository.findByAreaContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaLaboral> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return areaLaboralRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaLaboral> listarOrdenadasPorNombre() {
        return areaLaboralRepository.findAllByOrderByAreaAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarMecanicosPorAreaLaboral() {
        return areaLaboralRepository.contarMecanicosPorAreaLaboral();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaLaboral> listarAreasSinMecanicos() {
        return areaLaboralRepository.findAreasSinMecanicos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasTotalesPorAreaEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return areaLaboralRepository.horasTotalesPorAreaEnRangoFechas(inicio, fin);
    }
}
