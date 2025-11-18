package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.DetalleOrdenRepuestoId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.DetalleOrdenRepuestoService;
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
@RequestMapping("/api/detalle-orden-repuesto")
public class DetalleOrdenRepuestoController {

    private final DetalleOrdenRepuestoService detalleOrdenRepuestoService;

    public DetalleOrdenRepuestoController(DetalleOrdenRepuestoService detalleOrdenRepuestoService) {
        this.detalleOrdenRepuestoService = detalleOrdenRepuestoService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/detalle-orden-repuesto
    @GetMapping
    public ResponseEntity<List<DetalleOrdenRepuesto>> listarTodos() {
        return ResponseEntity.ok(detalleOrdenRepuestoService.listarTodos());
    }

    // GET /api/detalle-orden-repuesto/{idOrdenTrabajo}/{idRepuesto}
    @GetMapping("/{idOrdenTrabajo}/{idRepuesto}")
    public ResponseEntity<DetalleOrdenRepuesto> obtenerPorId(@PathVariable Integer idOrdenTrabajo,
                                                             @PathVariable Integer idRepuesto) {
        DetalleOrdenRepuestoId id = new DetalleOrdenRepuestoId(idOrdenTrabajo, idRepuesto);
        DetalleOrdenRepuesto detalle = detalleOrdenRepuestoService.obtenerPorId(id);
        return ResponseEntity.ok(detalle);
    }

    /**
     * POST /api/detalle-orden-repuesto
     *
     * Body esperado (ejemplo):
     * {
     *   "ordenTrabajo": { "idOrdenTrabajo": 1 },
     *   "repuesto": { "idRepuesto": 10 },
     *   "cantidad": 3,
     *   "subTotal": 150000.00
     * }
     *
     * El DetalleOrdenRepuestoId se arma en el Service si viene null.
     */
    @PostMapping
    public ResponseEntity<DetalleOrdenRepuesto> crear(@RequestBody DetalleOrdenRepuesto detalle) {
        DetalleOrdenRepuesto creado = detalleOrdenRepuestoService.crear(detalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/detalle-orden-repuesto/{idOrdenTrabajo}/{idRepuesto}
    @PutMapping("/{idOrdenTrabajo}/{idRepuesto}")
    public ResponseEntity<DetalleOrdenRepuesto> actualizar(@PathVariable Integer idOrdenTrabajo,
                                                           @PathVariable Integer idRepuesto,
                                                           @RequestBody DetalleOrdenRepuesto detalleActualizado) {
        DetalleOrdenRepuestoId id = new DetalleOrdenRepuestoId(idOrdenTrabajo, idRepuesto);
        DetalleOrdenRepuesto actualizado = detalleOrdenRepuestoService.actualizar(id, detalleActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/detalle-orden-repuesto/{idOrdenTrabajo}/{idRepuesto}
    @DeleteMapping("/{idOrdenTrabajo}/{idRepuesto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idOrdenTrabajo,
                                         @PathVariable Integer idRepuesto) {
        DetalleOrdenRepuestoId id = new DetalleOrdenRepuestoId(idOrdenTrabajo, idRepuesto);
        detalleOrdenRepuestoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/detalle-orden-repuesto/por-orden/{idOrdenTrabajo}
    @GetMapping("/por-orden/{idOrdenTrabajo}")
    public ResponseEntity<List<DetalleOrdenRepuesto>> listarPorIdOrdenTrabajo(
            @PathVariable Integer idOrdenTrabajo) {
        return ResponseEntity.ok(detalleOrdenRepuestoService.listarPorIdOrdenTrabajo(idOrdenTrabajo));
    }

    // GET /api/detalle-orden-repuesto/por-repuesto/{idRepuesto}
    @GetMapping("/por-repuesto/{idRepuesto}")
    public ResponseEntity<List<DetalleOrdenRepuesto>> listarPorIdRepuesto(
            @PathVariable Integer idRepuesto) {
        return ResponseEntity.ok(detalleOrdenRepuestoService.listarPorIdRepuesto(idRepuesto));
    }

    // GET /api/detalle-orden-repuesto/por-cantidad-minima?cantidad=5
    @GetMapping("/por-cantidad-minima")
    public ResponseEntity<List<DetalleOrdenRepuesto>> buscarPorCantidadMayorIgual(
            @RequestParam("cantidad") Short cantidadMinima) {
        return ResponseEntity.ok(detalleOrdenRepuestoService.buscarPorCantidadMayorIgual(cantidadMinima));
    }

    // GET /api/detalle-orden-repuesto/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<DetalleOrdenRepuesto>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(detalleOrdenRepuestoService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record ResumenRepuestosPorOrdenDTO(
            Integer idOrdenTrabajo,
            Long cantidadTotal,
            Double totalRepuestos
    ) {}

    public record RepuestoMasUtilizadoDTO(
            Integer idRepuesto,
            String nombreRepuesto,
            Long cantidadTotal,
            Double totalRepuestos
    ) {}

    public record ConsumoRepuestosClienteDTO(
            String idCliente,
            String nombreCliente,
            Long cantidadTotal,
            Double totalRepuestos
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/detalle-orden-repuesto/estadisticas/resumen-por-orden
    @GetMapping("/estadisticas/resumen-por-orden")
    public ResponseEntity<List<ResumenRepuestosPorOrdenDTO>> obtenerResumenRepuestosPorOrden() {
        List<Object[]> resultados = detalleOrdenRepuestoService.obtenerResumenRepuestosPorOrden();

        List<ResumenRepuestosPorOrdenDTO> respuesta = resultados.stream()
                .map(fila -> new ResumenRepuestosPorOrdenDTO(
                        ((Number) fila[0]).intValue(),                         // idOrdenTrabajo
                        fila[1] == null ? 0L : ((Number) fila[1]).longValue(),  // cantidadTotal
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()// totalRepuestos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/detalle-orden-repuesto/estadisticas/repuestos-mas-utilizados
    @GetMapping("/estadisticas/repuestos-mas-utilizados")
    public ResponseEntity<List<RepuestoMasUtilizadoDTO>> obtenerRepuestosMasUtilizados() {
        List<Object[]> resultados = detalleOrdenRepuestoService.obtenerRepuestosMasUtilizados();

        List<RepuestoMasUtilizadoDTO> respuesta = resultados.stream()
                .map(fila -> new RepuestoMasUtilizadoDTO(
                        ((Number) fila[0]).intValue(),                         // idRepuesto
                        (String) fila[1],                                       // nombreRepuesto
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),  // cantidadTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()// totalRepuestos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/detalle-orden-repuesto/estadisticas/consumo-por-cliente?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/estadisticas/consumo-por-cliente")
    public ResponseEntity<List<ConsumoRepuestosClienteDTO>> obtenerConsumoRepuestosPorClienteEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados = detalleOrdenRepuestoService
                .obtenerConsumoRepuestosPorClienteEnRangoFechas(inicio, fin);

        List<ConsumoRepuestosClienteDTO> respuesta = resultados.stream()
                .map(fila -> new ConsumoRepuestosClienteDTO(
                        (String) fila[0],                                       // idCliente
                        (String) fila[1],                                       // nombreCliente
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),  // cantidadTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()// totalRepuestos
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
