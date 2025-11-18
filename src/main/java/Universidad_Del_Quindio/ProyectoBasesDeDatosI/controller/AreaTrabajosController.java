package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajos;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaTrabajosId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AreaTrabajosService;
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
@RequestMapping("/api/area-trabajos")
public class AreaTrabajosController {

    private final AreaTrabajosService areaTrabajosService;

    public AreaTrabajosController(AreaTrabajosService areaTrabajosService) {
        this.areaTrabajosService = areaTrabajosService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/area-trabajos
    @GetMapping
    public ResponseEntity<List<AreaTrabajos>> listarTodos() {
        return ResponseEntity.ok(areaTrabajosService.listarTodos());
    }

    // GET /api/area-trabajos/{idAreaLaboral}/{idMecanico}
    @GetMapping("/{idAreaLaboral}/{idMecanico}")
    public ResponseEntity<AreaTrabajos> obtenerPorId(@PathVariable Integer idAreaLaboral,
                                                     @PathVariable String idMecanico) {
        AreaTrabajosId id = new AreaTrabajosId(idAreaLaboral, idMecanico);
        AreaTrabajos asignacion = areaTrabajosService.obtenerPorId(id);
        return ResponseEntity.ok(asignacion);
    }

    /**
     * POST /api/area-trabajos
     *
     * Body esperado (ejemplo):
     * {
     *   "areaLaboral": { "idAreaLaboral": 1 },
     *   "mecanico": { "idMecanico": "1234567890" }
     * }
     *
     * El ID compuesto se construye en el Service si viene null.
     */
    @PostMapping
    public ResponseEntity<AreaTrabajos> crear(@RequestBody AreaTrabajos entidad) {
        // Dejamos que el Service construya el AreaTrabajosId si viene null
        AreaTrabajos creada = areaTrabajosService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * PUT /api/area-trabajos/{idAreaLaboral}/{idMecanico}
     *
     * En esta relación con PK compuesta NO es buena práctica cambiar las claves.
     * Este endpoint existe por simetría, pero en tu caso no hay campos adicionales
     * que actualizar; si en el futuro agregas más columnas, se actualizarían aquí.
     */
    @PutMapping("/{idAreaLaboral}/{idMecanico}")
    public ResponseEntity<AreaTrabajos> actualizar(@PathVariable Integer idAreaLaboral,
                                                   @PathVariable String idMecanico,
                                                   @RequestBody AreaTrabajos entidadActualizada) {
        AreaTrabajosId id = new AreaTrabajosId(idAreaLaboral, idMecanico);
        AreaTrabajos actualizada = areaTrabajosService.actualizar(id, entidadActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/area-trabajos/{idAreaLaboral}/{idMecanico}
    @DeleteMapping("/{idAreaLaboral}/{idMecanico}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAreaLaboral,
                                         @PathVariable String idMecanico) {
        AreaTrabajosId id = new AreaTrabajosId(idAreaLaboral, idMecanico);
        areaTrabajosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/area-trabajos/por-area/{idAreaLaboral}
    @GetMapping("/por-area/{idAreaLaboral}")
    public ResponseEntity<List<AreaTrabajos>> listarPorIdAreaLaboral(@PathVariable Integer idAreaLaboral) {
        return ResponseEntity.ok(areaTrabajosService.listarPorIdAreaLaboral(idAreaLaboral));
    }

    // GET /api/area-trabajos/por-mecanico/{idMecanico}
    @GetMapping("/por-mecanico/{idMecanico}")
    public ResponseEntity<List<AreaTrabajos>> listarPorIdMecanico(@PathVariable String idMecanico) {
        return ResponseEntity.ok(areaTrabajosService.listarPorIdMecanico(idMecanico));
    }

    // GET /api/area-trabajos/creadas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creadas")
    public ResponseEntity<List<AreaTrabajos>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(areaTrabajosService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/area-trabajos/buscar-por-area?texto=meca
    @GetMapping("/buscar-por-area")
    public ResponseEntity<List<AreaTrabajos>> buscarPorNombreAreaConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(areaTrabajosService.buscarPorNombreAreaConteniendo(texto));
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record MecanicosPorAreaDTO(
            Integer idAreaLaboral,
            String nombreArea,
            Long cantidadMecanicos
    ) {}

    public record AreasPorMecanicoDTO(
            String idMecanico,
            Long cantidadAreas
    ) {}

    public record HorasCostoPorAreaDTO(
            Integer idAreaLaboral,
            String nombreArea,
            Double horasTotales,
            Double costoTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/area-trabajos/estadisticas/mecanicos-por-area
    @GetMapping("/estadisticas/mecanicos-por-area")
    public ResponseEntity<List<MecanicosPorAreaDTO>> contarMecanicosPorAreaLaboral() {
        List<Object[]> resultados = areaTrabajosService.contarMecanicosPorAreaLaboral();

        List<MecanicosPorAreaDTO> respuesta = resultados.stream()
                .map(fila -> new MecanicosPorAreaDTO(
                        ((Number) fila[0]).intValue(),                    // idAreaLaboral
                        (String) fila[1],                                  // nombreArea
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadMecanicos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/area-trabajos/estadisticas/areas-por-mecanico
    @GetMapping("/estadisticas/areas-por-mecanico")
    public ResponseEntity<List<AreasPorMecanicoDTO>> contarAreasPorMecanico() {
        List<Object[]> resultados = areaTrabajosService.contarAreasPorMecanico();

        List<AreasPorMecanicoDTO> respuesta = resultados.stream()
                .map(fila -> new AreasPorMecanicoDTO(
                        (String) fila[0],                                  // idMecanico
                        fila[1] == null ? 0L : ((Number) fila[1]).longValue() // cantidadAreas
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/area-trabajos/estadisticas/horas-costo-por-area?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/estadisticas/horas-costo-por-area")
    public ResponseEntity<List<HorasCostoPorAreaDTO>> obtenerHorasYCostoPorAreaEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados = areaTrabajosService.obtenerHorasYCostoPorAreaEnRangoFechas(inicio, fin);

        List<HorasCostoPorAreaDTO> respuesta = resultados.stream()
                .map(fila -> new HorasCostoPorAreaDTO(
                        ((Number) fila[0]).intValue(),                         // idAreaLaboral
                        (String) fila[1],                                       // nombreArea
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue(), // horasTotales
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()  // costoTotal
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
