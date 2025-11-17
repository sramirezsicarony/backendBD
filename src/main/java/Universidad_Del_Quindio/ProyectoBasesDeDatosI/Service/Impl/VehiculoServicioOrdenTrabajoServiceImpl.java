package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.VehiculoServicioOrdenTrabajo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.VehiculoServicioOrdenTrabajoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.VehiculoServicioOrdenTrabajoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VehiculoServicioOrdenTrabajoServiceImpl implements VehiculoServicioOrdenTrabajoService {

    private final VehiculoServicioOrdenTrabajoRepository vsoRepository;

    // Inyección por constructor (recomendada)
    public VehiculoServicioOrdenTrabajoServiceImpl(
            VehiculoServicioOrdenTrabajoRepository vsoRepository
    ) {
        this.vsoRepository = vsoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoServicioOrdenTrabajo> listarTodos() {
        return vsoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoServicioOrdenTrabajo obtenerPorId(Integer idVehiculoServicioOrdenTrabajo) {
        return vsoRepository.findById(idVehiculoServicioOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un registro vehículo–servicio–orden con id: "
                                + idVehiculoServicioOrdenTrabajo
                ));
    }

    @Override
    public VehiculoServicioOrdenTrabajo crear(VehiculoServicioOrdenTrabajo entidad) {
        // idVehiculoServicioOrdenTrabajo es autogenerado (IDENTITY)
        return vsoRepository.save(entidad);
    }

    @Override
    public VehiculoServicioOrdenTrabajo actualizar(Integer idVehiculoServicioOrdenTrabajo,
                                                   VehiculoServicioOrdenTrabajo entidadActualizada) {

        VehiculoServicioOrdenTrabajo existente = vsoRepository.findById(idVehiculoServicioOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un registro vehículo–servicio–orden con id: "
                                + idVehiculoServicioOrdenTrabajo
                ));

        // Actualizamos solo los campos modificables
        existente.setVehiculo(entidadActualizada.getVehiculo());
        existente.setServicio(entidadActualizada.getServicio());
        existente.setOrdenTrabajo(entidadActualizada.getOrdenTrabajo());
        existente.setFechaDeEjecucion(entidadActualizada.getFechaDeEjecucion());

        return vsoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idVehiculoServicioOrdenTrabajo) {
        VehiculoServicioOrdenTrabajo existente = vsoRepository.findById(idVehiculoServicioOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un registro vehículo–servicio–orden con id: "
                                + idVehiculoServicioOrdenTrabajo
                ));

        vsoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoServicioOrdenTrabajo> listarPorIdVehiculo(String idVehiculo) {
        return vsoRepository.findByVehiculo_IdVehiculo(idVehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoServicioOrdenTrabajo> listarPorIdServicio(Integer idServicio) {
        return vsoRepository.findByServicio_IdServicio(idServicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoServicioOrdenTrabajo> listarPorIdOrdenTrabajo(Integer idOrdenTrabajo) {
        return vsoRepository.findByOrdenTrabajo_IdOrdenTrabajo(idOrdenTrabajo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoServicioOrdenTrabajo> buscarPorRangoFechaEjecucion(LocalDate inicio, LocalDate fin) {
        return vsoRepository.findByFechaDeEjecucionBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarEjecucionesPorServicio() {
        return vsoRepository.contarEjecucionesPorServicio();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerCantidadServiciosPorVehiculoEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return vsoRepository.cantidadServiciosPorVehiculoEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerServiciosPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return vsoRepository.serviciosPorClienteEnRangoFechas(inicio, fin);
    }
}
