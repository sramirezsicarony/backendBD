package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.AreaLaboral;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AreaLaboralService;
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
@RequestMapping("/api/areas-laborales")
public class AreaLaboralController {

    private final AreaLaboralService areaLaboralService;

    public AreaLaboralController(AreaLaboralService areaLaboralService) {
        this.areaLaboralService = areaLaboralService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/areas-laborales
    @GetMapping
    public ResponseEntity<List<AreaLaboral>> listarTodas() {
        return ResponseEntity.ok(areaLaboralService.listarTodas());
    }

    // GET /api/areas-laborales/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AreaLaboral> obtenerPorId(@PathVariable("id") Integer idAreaLaboral) {
        AreaLaboral area = areaLaboralService.obtenerPorId(idAreaLaboral);
        return ResponseEntity.ok(area);
    }

    // POST /api/areas-laborales
    @PostMapping
    public ResponseEntity<AreaLaboral> crear(@RequestBody AreaLaboral areaLaboral) {
        // El id lo genera la BD
        areaLaboral.setIdAreaLaboral(null);
        AreaLaboral creada = areaLaboralService.crear(areaLaboral);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/areas-laborales/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AreaLaboral> actualizar(@PathVariable("id") Integer idAreaLaboral,
                                                  @RequestBody AreaLaboral areaLaboralActualizada) {
        AreaLaboral actualizada = areaLaboralService.actualizar(idAreaLaboral, areaLaboralActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/areas-laborales/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer idAreaLaboral) {
        areaLaboralService.eliminar(idAreaLaboral);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/areas-laborales/por-nombre?nombre=Latoneria
    @GetMapping("/por-nombre")
    public ResponseEntity<AreaLaboral> obtenerPorNombre(@RequestParam("nombre") String nombreArea) {
        AreaLaboral area = areaLaboralService.obtenerPorNombre(nombreArea);
        if (area == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(area);
    }

    // GET /api/areas-laborales/buscar?texto=meca
    @GetMapping("/buscar")
    public ResponseEntity<List<AreaLaboral>> buscarPorNombreConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(areaLaboralService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/areas-laborales/creadas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creadas")
    public ResponseEntity<List<AreaLaboral>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(areaLaboralService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/areas-laborales/ordenadas
    @GetMapping("/ordenadas")
    public ResponseEntity<List<AreaLaboral>> listarOrdenadasPorNombre() {
        return ResponseEntity.ok(areaLaboralService.listarOrdenadasPorNombre());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    // Mécanicos por área
    public record AreaLaboralMecanicosDTO(
            Integer idAreaLaboral,
            String nombreArea,
            Long cantidadMecanicos
    ) {}

    // Horas totales por área
    public record AreaLaboralHorasDTO(
            Integer idAreaLaboral,
            String nombreArea,
            Double horasTotales
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/areas-laborales/estadisticas/mecanicos-por-area
    public static final String MECANICOS_POR_AREA_PATH = "/estadisticas/mecanicos-por-area";

    @GetMapping(MECANICOS_POR_AREA_PATH)
    public ResponseEntity<List<AreaLaboralMecanicosDTO>> contarMecanicosPorAreaLaboral() {
        List<Object[]> resultados = areaLaboralService.contarMecanicosPorAreaLaboral();

        List<AreaLaboralMecanicosDTO> respuesta = resultados.stream()
                .map(fila -> new AreaLaboralMecanicosDTO(
                        ((Number) fila[0]).intValue(),           // idAreaLaboral
                        (String) fila[1],                         // nombreArea
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadMecanicos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/areas-laborales/sin-mecanicos
    @GetMapping("/sin-mecanicos")
    public ResponseEntity<List<AreaLaboral>> listarAreasSinMecanicos() {
        return ResponseEntity.ok(areaLaboralService.listarAreasSinMecanicos());
    }

    // GET /api/areas-laborales/estadisticas/horas-por-area?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/estadisticas/horas-por-area")
    public ResponseEntity<List<AreaLaboralHorasDTO>> obtenerHorasTotalesPorAreaEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados = areaLaboralService.obtenerHorasTotalesPorAreaEnRangoFechas(inicio, fin);

        List<AreaLaboralHorasDTO> respuesta = resultados.stream()
                .map(fila -> new AreaLaboralHorasDTO(
                        ((Number) fila[0]).intValue(),              // idAreaLaboral
                        (String) fila[1],                            // nombreArea
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
