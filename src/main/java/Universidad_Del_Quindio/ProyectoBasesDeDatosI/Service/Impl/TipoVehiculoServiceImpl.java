package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;


import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.TipoVehiculo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.TipoVehiculoRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.TipoVehiculoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TipoVehiculoServiceImpl implements TipoVehiculoService {

    private final TipoVehiculoRepository tipoVehiculoRepository;

    // Inyección por constructor (recomendada)
    public TipoVehiculoServiceImpl(TipoVehiculoRepository tipoVehiculoRepository) {
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> listarTodos() {
        return tipoVehiculoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoVehiculo obtenerPorId(Integer idTipoVehiculo) {
        return tipoVehiculoRepository.findById(idTipoVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un tipo de vehículo con id: " + idTipoVehiculo
                ));
    }

    @Override
    public TipoVehiculo crear(TipoVehiculo tipoVehiculo) {
        // id_tipo_vehiculo es autogenerado por la BD
        return tipoVehiculoRepository.save(tipoVehiculo);
    }

    @Override
    public TipoVehiculo actualizar(Integer idTipoVehiculo, TipoVehiculo tipoActualizado) {
        TipoVehiculo existente = tipoVehiculoRepository.findById(idTipoVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un tipo de vehículo con id: " + idTipoVehiculo
                ));

        // Solo tiene un campo modificable: tipo
        existente.setTipo(tipoActualizado.getTipo());

        return tipoVehiculoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idTipoVehiculo) {
        TipoVehiculo existente = tipoVehiculoRepository.findById(idTipoVehiculo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un tipo de vehículo con id: " + idTipoVehiculo
                ));

        tipoVehiculoRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public TipoVehiculo buscarPorTipoExacto(String tipo) {
        return tipoVehiculoRepository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> buscarPorTipoConteniendo(String texto) {
        return tipoVehiculoRepository.findByTipoContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return tipoVehiculoRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> listarOrdenadosPorNombre() {
        return tipoVehiculoRepository.findAllByOrderByTipoAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarVehiculosPorTipo() {
        return tipoVehiculoRepository.contarVehiculosPorTipo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoVehiculo> buscarTiposSinVehiculos() {
        return tipoVehiculoRepository.findTiposSinVehiculos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarVehiculosPorTipoEnRangoAnio(int anioInicio, int anioFin) {
        return tipoVehiculoRepository.contarVehiculosPorTipoEnRangoAnio(anioInicio, anioFin);
    }
}
