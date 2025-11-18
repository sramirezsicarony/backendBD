package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Rol;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/roles
    @GetMapping
    public ResponseEntity<List<Rol>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    // GET /api/roles/{idRol}
    @GetMapping("/{idRol}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Byte idRol) {
        Rol rol = rolService.obtenerPorId(idRol);
        return ResponseEntity.ok(rol);
    }

    /**
     * POST /api/roles
     *
     * Body ejemplo:
     * {
     *   "rol": "JEFE DE TALLER"
     * }
     */
    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody Rol rol) {
        Rol creado = rolService.crear(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/roles/{idRol}
    @PutMapping("/{idRol}")
    public ResponseEntity<Rol> actualizar(@PathVariable Byte idRol,
                                          @RequestBody Rol rolActualizado) {
        Rol actualizado = rolService.actualizar(idRol, rolActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/roles/{idRol}
    @DeleteMapping("/{idRol}")
    public ResponseEntity<Void> eliminar(@PathVariable Byte idRol) {
        rolService.eliminar(idRol);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/roles/por-nombre?nombre=JEFE%20DE%20TALLER
    @GetMapping("/por-nombre")
    public ResponseEntity<Rol> obtenerPorNombre(@RequestParam("nombre") String nombreRol) {
        Rol rol = rolService.obtenerPorNombre(nombreRol);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rol);
    }

    // GET /api/roles/buscar?texto=jefe
    @GetMapping("/buscar")
    public ResponseEntity<List<Rol>> buscarPorNombreConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(rolService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/roles/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<Rol>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(
                rolService.buscarPorRangoFechasCreacion(inicio, fin)
        );
    }

    // GET /api/roles/ordenados
    @GetMapping("/ordenados")
    public ResponseEntity<List<Rol>> listarOrdenadosPorNombre() {
        return ResponseEntity.ok(rolService.listarOrdenadosPorNombre());
    }

    // GET /api/roles/sin-asignaciones
    @GetMapping("/sin-asignaciones")
    public ResponseEntity<List<Rol>> listarRolesSinAsignaciones() {
        return ResponseEntity.ok(rolService.listarRolesSinAsignaciones());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record RolAsignacionesDTO(
            Byte idRol,
            String nombreRol,
            Long cantidadAsignaciones
    ) {}

    public record HorasPorRolDTO(
            Byte idRol,
            String nombreRol,
            Double horasTotales
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/roles/estadisticas/asignaciones
    @GetMapping("/estadisticas/asignaciones")
    public ResponseEntity<List<RolAsignacionesDTO>> contarAsignacionesPorRol() {
        List<Object[]> resultados = rolService.contarAsignacionesPorRol();

        List<RolAsignacionesDTO> respuesta = resultados.stream()
                .map(fila -> new RolAsignacionesDTO(
                        ((Number) fila[0]).byteValue(),                 // idRol
                        (String) fila[1],                               // nombreRol
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadAsignaciones
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/roles/estadisticas/horas?inicio=...&fin=...
    @GetMapping("/estadisticas/horas")
    public ResponseEntity<List<HorasPorRolDTO>> obtenerHorasTotalesPorRolEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados = rolService.obtenerHorasTotalesPorRolEnRangoFechas(inicio, fin);

        List<HorasPorRolDTO> respuesta = resultados.stream()
                .map(fila -> new HorasPorRolDTO(
                        ((Number) fila[0]).byteValue(),                      // idRol
                        (String) fila[1],                                     // nombreRol
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue() // horasTotales
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // ================== MANEJO DE ERRORES ==================

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> manejarEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
