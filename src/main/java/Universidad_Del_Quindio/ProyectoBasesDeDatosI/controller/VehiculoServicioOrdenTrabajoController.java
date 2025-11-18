package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.VehiculoServicioOrdenTrabajo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.VehiculoServicioOrdenTrabajoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vso")
public class VehiculoServicioOrdenTrabajoController {

    private final VehiculoServicioOrdenTrabajoService vsoService;

    public VehiculoServicioOrdenTrabajoController(VehiculoServicioOrdenTrabajoService vsoService) {
        this.vsoService = vsoService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/vso
    @GetMapping
    public ResponseEntity<List<VehiculoServicioOrdenTrabajo>> listarTodos() {
        return ResponseEntity.ok(vsoService.listarTodos());
    }

    // GET /api/vso/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoServicioOrdenTrabajo> obtenerPorId(@PathVariable Integer id) {
        VehiculoServicioOrdenTrabajo entidad = vsoService.obtenerPorId(id);
        return ResponseEntity.ok(entidad);
    }

    /**
     * POST /api/vso
     *
     * Body ejemplo:
     * {
     *   "vehiculo": { "idVehiculo": "ABC123" },
     *   "servicio": { "idServicio": 1 },
     *   "ordenTrabajo": { "idOrdenTrabajo": 10 },
     *   "fechaDeEjecucion": "2025-11-17"
     * }
     */
    @PostMapping
    public ResponseEntity<VehiculoServicioOrdenTrabajo> crear(@RequestBody VehiculoServicioOrdenTrabajo entidad) {
        VehiculoServicioOrdenTrabajo creado = vsoService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/vso/{id}
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoServicioOrdenTrabajo> actualizar(@PathVariable Integer id,
                                                                   @RequestBody VehiculoServicioOrdenTrabajo entidadActualizada) {
        VehiculoServicioOrdenTrabajo actualizada = vsoService.actualizar(id, entidadActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/vso/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        vsoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/vso/por-vehiculo/{idVehiculo}
    @GetMapping("/por-vehiculo/{idVehiculo}")
    public ResponseEntity<List<VehiculoServicioOrdenTrabajo>> listarPorIdVehiculo(@PathVariable String idVehiculo) {
        return ResponseEntity.ok(vsoService.listarPorIdVehiculo(idVehiculo));
    }

    // GET /api/vso/por-servicio/{idServicio}
    @GetMapping("/por-servicio/{idServicio}")
    public ResponseEntity<List<VehiculoServicioOrdenTrabajo>> listarPorIdServicio(@PathVariable Integer idServicio) {
        return ResponseEntity.ok(vsoService.listarPorIdServicio(idServicio));
    }

    // GET /api/vso/por-orden/{idOrdenTrabajo}
    @GetMapping("/por-orden/{idOrdenTrabajo}")
    public ResponseEntity<List<VehiculoServicioOrdenTrabajo>> listarPorIdOrdenTrabajo(@PathVariable Integer idOrdenTrabajo) {
        return ResponseEntity.ok(vsoService.listarPorIdOrdenTrabajo(idOrdenTrabajo));
    }

    // GET /api/vso/por-fecha-ejecucion?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/por-fecha-ejecucion")
    public ResponseEntity<List<VehiculoServicioOrdenTrabajo>> buscarPorRangoFechaEjecucion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        return ResponseEntity.ok(
                vsoService.buscarPorRangoFechaEjecucion(inicio, fin)
        );
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record EjecucionesServicioDTO(
            Integer idServicio,
            String nombreServicio,
            Long cantidadEjecuciones
    ) {}

    public record ServiciosPorVehiculoRangoDTO(
            String idVehiculo,
            Long cantidadServicios
    ) {}

    public record ServiciosPorClienteRangoDTO(
            String idCliente,
            String nombreCliente,
            Long cantidadServicios
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/vso/estadisticas/ejecuciones-por-servicio
    public static final String EJECUCIONES_POR_SERVICIO_PATH = "/estadisticas/ejecuciones-por-servicio";

    @GetMapping(EJECUCIONES_POR_SERVICIO_PATH)
    public ResponseEntity<List<EjecucionesServicioDTO>> contarEjecucionesPorServicio() {
        List<Object[]> resultados = vsoService.contarEjecucionesPorServicio();

        List<EjecucionesServicioDTO> respuesta = resultados.stream()
                .map(fila -> new EjecucionesServicioDTO(
                        ((Number) fila[0]).intValue(),                        // idServicio
                        (String) fila[1],                                     // nombreServicio
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadEjecuciones
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/vso/estadisticas/servicios-por-vehiculo-rango?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/estadisticas/servicios-por-vehiculo-rango")
    public ResponseEntity<List<ServiciosPorVehiculoRangoDTO>> obtenerCantidadServiciosPorVehiculoEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados = vsoService.obtenerCantidadServiciosPorVehiculoEnRangoFechas(inicio, fin);

        List<ServiciosPorVehiculoRangoDTO> respuesta = resultados.stream()
                .map(fila -> new ServiciosPorVehiculoRangoDTO(
                        (String) fila[0],                                     // idVehiculo
                        fila[1] == null ? 0L : ((Number) fila[1]).longValue() // cantidadServicios
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/vso/estadisticas/servicios-por-cliente-rango?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/estadisticas/servicios-por-cliente-rango")
    public ResponseEntity<List<ServiciosPorClienteRangoDTO>> obtenerServiciosPorClienteEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados = vsoService.obtenerServiciosPorClienteEnRangoFechas(inicio, fin);

        List<ServiciosPorClienteRangoDTO> respuesta = resultados.stream()
                .map(fila -> new ServiciosPorClienteRangoDTO(
                        (String) fila[0],                                     // idCliente
                        (String) fila[1],                                     // nombreCliente
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue() // cantidadServicios
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
