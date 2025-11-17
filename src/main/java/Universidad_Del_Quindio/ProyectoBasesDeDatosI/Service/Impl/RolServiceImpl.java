package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Rol;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.RolRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    // Inyección por constructor (recomendada)
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerPorId(Byte idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un rol con id: " + idRol
                ));
    }

    @Override
    public Rol crear(Rol rol) {
        // idRol es autogenerado, por lo que no se debe asignar manualmente
        return rolRepository.save(rol);
    }

    @Override
    public Rol actualizar(Byte idRol, Rol rolActualizado) {
        Rol existente = rolRepository.findById(idRol)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe un rol con id: " + idRol
                ));

        // Solo actualizamos los campos que el usuario puede cambiar
        existente.setRol(rolActualizado.getRol());

        return rolRepository.save(existente);
    }

    @Override
    public void eliminar(Byte idRol) {
        Rol existente = rolRepository.findById(idRol)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe un rol con id: " + idRol
                ));

        rolRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerPorNombre(String nombreRol) {
        return rolRepository.findByRol(nombreRol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> buscarPorNombreConteniendo(String texto) {
        return rolRepository.findByRolContainingIgnoreCase(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> buscarPorRangoFechasCreacion(LocalDateTime inicio, LocalDateTime fin) {
        return rolRepository.findByCreatedAtBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarOrdenadosPorNombre() {
        return rolRepository.findAllByOrderByRolAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarAsignacionesPorRol() {
        return rolRepository.contarAsignacionesPorRol();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRolesSinAsignaciones() {
        return rolRepository.findRolesSinAsignaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerHorasTotalesPorRolEnRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return rolRepository.horasTotalesPorRolEnRangoFechas(inicio, fin);
    }
}
