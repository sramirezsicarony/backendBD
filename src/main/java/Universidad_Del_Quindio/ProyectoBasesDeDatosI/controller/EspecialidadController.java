package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Especialidad;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EspecialidadService;
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
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/especialidades
    @GetMapping
    public ResponseEntity<List<Especialidad>> listarTodas() {
        return ResponseEntity.ok(especialidadService.listarTodas());
    }

    // GET /api/especialidades/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtenerPorId(@PathVariable Integer id) {
        Especialidad especialidad = especialidadService.obtenerPorId(id);
        return ResponseEntity.ok(especialidad);
    }

    /**
     * POST /api/especialidades
     *
     * Body esperado (ejemplo):
     * {
     *   "nombreEspecialidad": "Electricista automotriz",
     *   "descripcion": "Especialista en sistema eléctrico del vehículo"
     * }
     */
    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad especialidad) {
        Especialidad creada = especialidadService.crear(especialidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/especialidades/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizar(@PathVariable Integer id,
                                                   @RequestBody Especialidad especialidadActualizada) {
        Especialidad actualizada = especialidadService.actualizar(id, especialidadActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/especialidades/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/especialidades/buscar/nombre-exacto?nombre=...
    @GetMapping("/buscar/nombre-exacto")
    public ResponseEntity<Especialidad> obtenerPorNombre(
            @RequestParam("nombre") String nombreEspecialidad) {

        Especialidad especialidad = especialidadService.obtenerPorNombre(nombreEspecialidad);
        if (especialidad == null) {
            throw new EntityNotFoundException(
                    "No se encontró una especialidad con nombre: " + nombreEspecialidad
            );
        }
        return ResponseEntity.ok(especialidad);
    }

    // GET /api/especialidades/buscar/nombre-contiene?texto=...
    @GetMapping("/buscar/nombre-contiene")
    public ResponseEntity<List<Especialidad>> buscarPorNombreConteniendo(
            @RequestParam("texto") String texto) {

        return ResponseEntity.ok(especialidadService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/especialidades/creadas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creadas")
    public ResponseEntity<List<Especialidad>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(especialidadService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/especialidades/ordenadas/nombre
    @GetMapping("/ordenadas/nombre")
    public ResponseEntity<List<Especialidad>> listarOrdenadasPorNombre() {
        return ResponseEntity.ok(especialidadService.listarOrdenadasPorNombre());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record MecanicosPorEspecialidadDTO(
            Integer idEspecialidad,
            String nombreEspecialidad,
            Long cantidadMecanicos
    ) {}

    public record CostoManoObraPorEspecialidadDTO(
            Integer idEspecialidad,
            String nombreEspecialidad,
            Double costoTotalManoObra
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/especialidades/estadisticas/mecanicos-por-especialidad
    @GetMapping("/estadisticas/mecanicos-por-especialidad")
    public ResponseEntity<List<MecanicosPorEspecialidadDTO>> contarMecanicosPorEspecialidad() {
        List<Object[]> resultados = especialidadService.contarMecanicosPorEspecialidad();

        List<MecanicosPorEspecialidadDTO> respuesta = resultados.stream()
                .map(fila -> new MecanicosPorEspecialidadDTO(
                        ((Number) fila[0]).intValue(),                        // idEspecialidad
                        (String) fila[1],                                      // nombreEspecialidad
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadMecanicos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/especialidades/sin-mecanicos
    @GetMapping("/sin-mecanicos")
    public ResponseEntity<List<Especialidad>> listarEspecialidadesSinMecanicos() {
        return ResponseEntity.ok(especialidadService.listarEspecialidadesSinMecanicos());
    }

    // GET /api/especialidades/estadisticas/costo-mano-obra?inicio=...&fin=...
    @GetMapping("/estadisticas/costo-mano-obra")
    public ResponseEntity<List<CostoManoObraPorEspecialidadDTO>> obtenerCostoTotalManoObraPorEspecialidadEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados =
                especialidadService.obtenerCostoTotalManoObraPorEspecialidadEnRangoFechas(inicio, fin);

        List<CostoManoObraPorEspecialidadDTO> respuesta = resultados.stream()
                .map(fila -> new CostoManoObraPorEspecialidadDTO(
                        ((Number) fila[0]).intValue(),                         // idEspecialidad
                        (String) fila[1],                                       // nombreEspecialidad
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()// costoTotalManoObra
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
