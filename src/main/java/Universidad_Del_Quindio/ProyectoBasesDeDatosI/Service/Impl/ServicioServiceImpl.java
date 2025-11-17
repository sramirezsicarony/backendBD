package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Servicio;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.ServicioRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ServicioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    // Inyección por constructor (recomendada)
    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio obtenerPorId(Integer idServicio) {
        return servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un servicio con id: " + idServicio
                ));
    }

    @Override
    public Servicio crear(Servicio servicio) {
        // idServicio es autogenerado (IDENTITY), no se debe asignar manualmente
        return servicioRepository.save(servicio);
    }

    @Override
    public Servicio actualizar(Integer idServicio, Servicio servicioActualizado) {
        Servicio existente = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un servicio con id: " + idServicio
                ));

        // Actualizamos solo los campos modificables
        existente.setServicio(servicioActualizado.getServicio());
        existente.setDescripcion(servicioActualizado.getDescripcion());

        return servicioRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idServicio) {
        Servicio existente = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un servicio con id: " + idServicio
                ));

        servicioRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Servicio obtenerPorNombre(String nombreServicio) {
        return servicioRepository.findByServicio(nombreServicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> buscarPorNombreConteniendo(String texto) {
        return servicioRepository.findByServicioContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return servicioRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarOrdenadosPorNombre() {
        return servicioRepository.findAllByOrderByServicioAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarEjecucionesPorServicio() {
        return servicioRepository.contarEjecucionesPorServicio();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServiciosNuncaEjecutados() {
        return servicioRepository.findServiciosNuncaEjecutados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerUsoDeServiciosEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return servicioRepository.usoDeServiciosEnRangoFechas(inicio, fin);
    }
}
