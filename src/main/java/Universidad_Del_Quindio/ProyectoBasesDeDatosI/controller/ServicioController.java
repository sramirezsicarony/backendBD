package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Servicio;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ServicioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/servicios
    @GetMapping
    public ResponseEntity<List<Servicio>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    // GET /api/servicios/{idServicio}
    @GetMapping("/{idServicio}")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Integer idServicio) {
        Servicio servicio = servicioService.obtenerPorId(idServicio);
        return ResponseEntity.ok(servicio);
    }

    /**
     * POST /api/servicios
     *
     * Body ejemplo:
     * {
     *   "servicio": "Cambio de aceite",
     *   "descripcion": "Cambio de aceite de motor y filtro"
     * }
     */
    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        Servicio creado = servicioService.crear(servicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/servicios/{idServicio}
    @PutMapping("/{idServicio}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Integer idServicio,
                                               @RequestBody Servicio servicioActualizado) {
        Servicio actualizado = servicioService.actualizar(idServicio, servicioActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/servicios/{idServicio}
    @DeleteMapping("/{idServicio}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idServicio) {
        servicioService.eliminar(idServicio);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/servicios/por-nombre?nombre=Cambio%20de%20aceite
    @GetMapping("/por-nombre")
    public ResponseEntity<Servicio> obtenerPorNombre(@RequestParam("nombre") String nombreServicio) {
        Servicio servicio = servicioService.obtenerPorNombre(nombreServicio);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(servicio);
    }

    // GET /api/servicios/buscar?texto=aceite
    @GetMapping("/buscar")
    public ResponseEntity<List<Servicio>> buscarPorNombreConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(servicioService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/servicios/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<Servicio>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(
                servicioService.buscarPorRangoFechasCreacion(inicio, fin)
        );
    }

    // GET /api/servicios/ordenados
    @GetMapping("/ordenados")
    public ResponseEntity<List<Servicio>> listarOrdenadosPorNombre() {
        return ResponseEntity.ok(servicioService.listarOrdenadosPorNombre());
    }

    // GET /api/servicios/nunca-ejecutados
    @GetMapping("/nunca-ejecutados")
    public ResponseEntity<List<Servicio>> listarServiciosNuncaEjecutados() {
        return ResponseEntity.ok(servicioService.listarServiciosNuncaEjecutados());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record ServicioEjecucionesDTO(
            Integer idServicio,
            String nombreServicio,
            Long cantidadEjecuciones
    ) {}

    public record UsoServicioDTO(
            Integer idServicio,
            String nombreServicio,
            Long ejecuciones,
            Long vehiculosDistintos
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/servicios/estadisticas/ejecuciones
    public static final String ESTADISTICAS_EJECUCIONES_PATH = "/estadisticas/ejecuciones";

    @GetMapping(ESTADISTICAS_EJECUCIONES_PATH)
    public ResponseEntity<List<ServicioEjecucionesDTO>> contarEjecucionesPorServicio() {
        List<Object[]> resultados = servicioService.contarEjecucionesPorServicio();

        List<ServicioEjecucionesDTO> respuesta = resultados.stream()
                .map(fila -> new ServicioEjecucionesDTO(
                        ((Number) fila[0]).intValue(),                    // idServicio
                        (String) fila[1],                                  // nombreServicio
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadEjecuciones
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/servicios/estadisticas/uso?inicio=2025-01-01&fin=2025-12-31
    public static final String ESTADISTICAS_USO_PATH = "/estadisticas/uso";

    @GetMapping(ESTADISTICAS_USO_PATH)
    public ResponseEntity<List<UsoServicioDTO>> obtenerUsoDeServiciosEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados = servicioService.obtenerUsoDeServiciosEnRangoFechas(inicio, fin);

        List<UsoServicioDTO> respuesta = resultados.stream()
                .map(fila -> new UsoServicioDTO(
                        ((Number) fila[0]).intValue(),                        // idServicio
                        (String) fila[1],                                     // nombreServicio
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(), // ejecuciones
                        fila[3] == null ? 0L : ((Number) fila[3]).longValue()  // vehiculosDistintos
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
