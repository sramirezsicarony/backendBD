package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.OrdenTrabajoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.OrdenTrabajoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrdenTrabajoServiceImpl implements OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;

    // Inyección por constructor (recomendada)
    public OrdenTrabajoServiceImpl(OrdenTrabajoRepository ordenTrabajoRepository) {
        this.ordenTrabajoRepository = ordenTrabajoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarTodas() {
        return ordenTrabajoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTrabajo obtenerPorId(Integer idOrdenTrabajo) {
        return ordenTrabajoRepository.findById(idOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró una orden de trabajo con id: " + idOrdenTrabajo
                ));
    }

    @Override
    public OrdenTrabajo crear(OrdenTrabajo ordenTrabajo) {
        // idOrdenTrabajo es autogenerado (IDENTITY), no se debe asignar manualmente
        return ordenTrabajoRepository.save(ordenTrabajo);
    }

    @Override
    public OrdenTrabajo actualizar(Integer idOrdenTrabajo, OrdenTrabajo ordenTrabajoActualizada) {
        OrdenTrabajo existente = ordenTrabajoRepository.findById(idOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe una orden de trabajo con id: " + idOrdenTrabajo
                ));

        // Actualizamos solo los campos modificables
        existente.setVehiculo(ordenTrabajoActualizada.getVehiculo());
        existente.setDiagnosticoInicial(ordenTrabajoActualizada.getDiagnosticoInicial());
        existente.setFechaIngreso(ordenTrabajoActualizada.getFechaIngreso());
        existente.setEstadoOrden(ordenTrabajoActualizada.getEstadoOrden());
        existente.setFechaSalida(ordenTrabajoActualizada.getFechaSalida());

        return ordenTrabajoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idOrdenTrabajo) {
        OrdenTrabajo existente = ordenTrabajoRepository.findById(idOrdenTrabajo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe una orden de trabajo con id: " + idOrdenTrabajo
                ));

        ordenTrabajoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarPorIdVehiculo(String idVehiculo) {
        return ordenTrabajoRepository.findByVehiculo_IdVehiculo(idVehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarPorIdEstadoOrden(Byte idEstadoOrden) {
        return ordenTrabajoRepository.findByEstadoOrden_IdEstadoOrden(idEstadoOrden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> buscarPorRangoFechaIngreso(LocalDate inicio, LocalDate fin) {
        return ordenTrabajoRepository.findByFechaIngresoBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesAbiertas() {
        return ordenTrabajoRepository.findByFechaSalidaIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarOrdenesPorEstado() {
        return ordenTrabajoRepository.contarOrdenesPorEstado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarOrdenesPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return ordenTrabajoRepository.contarOrdenesPorClienteEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenCostosPorOrdenEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return ordenTrabajoRepository.resumenCostosPorOrdenEnRangoFechas(inicio, fin);
    }
}
