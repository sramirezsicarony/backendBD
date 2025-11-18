package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoOrden;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EstadoOrdenService;
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
@RequestMapping("/api/estados-orden")
public class EstadoOrdenController {

    private final EstadoOrdenService estadoOrdenService;

    public EstadoOrdenController(EstadoOrdenService estadoOrdenService) {
        this.estadoOrdenService = estadoOrdenService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/estados-orden
    @GetMapping
    public ResponseEntity<List<EstadoOrden>> listarTodos() {
        return ResponseEntity.ok(estadoOrdenService.listarTodos());
    }

    // GET /api/estados-orden/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EstadoOrden> obtenerPorId(@PathVariable Byte id) {
        EstadoOrden estado = estadoOrdenService.obtenerPorId(id);
        return ResponseEntity.ok(estado);
    }

    /**
     * POST /api/estados-orden
     *
     * Body ejemplo:
     * {
     *   "estado": "EN_PROCESO"
     * }
     */
    @PostMapping
    public ResponseEntity<EstadoOrden> crear(@RequestBody EstadoOrden estadoOrden) {
        EstadoOrden creado = estadoOrdenService.crear(estadoOrden);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/estados-orden/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EstadoOrden> actualizar(@PathVariable Byte id,
                                                  @RequestBody EstadoOrden estadoActualizado) {
        EstadoOrden actualizado = estadoOrdenService.actualizar(id, estadoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/estados-orden/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Byte id) {
        estadoOrdenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/estados-orden/buscar/nombre-exacto?nombre=...
    @GetMapping("/buscar/nombre-exacto")
    public ResponseEntity<EstadoOrden> obtenerPorNombre(
            @RequestParam("nombre") String nombreEstado) {

        EstadoOrden estado = estadoOrdenService.obtenerPorNombre(nombreEstado);
        if (estado == null) {
            throw new EntityNotFoundException(
                    "No se encontró un estado de orden con nombre: " + nombreEstado
            );
        }
        return ResponseEntity.ok(estado);
    }

    // GET /api/estados-orden/buscar/nombre-contiene?texto=...
    @GetMapping("/buscar/nombre-contiene")
    public ResponseEntity<List<EstadoOrden>> buscarPorNombreConteniendo(
            @RequestParam("texto") String texto) {

        return ResponseEntity.ok(estadoOrdenService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/estados-orden/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<EstadoOrden>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(estadoOrdenService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/estados-orden/ordenados/nombre
    @GetMapping("/ordenados/nombre")
    public ResponseEntity<List<EstadoOrden>> listarOrdenadosPorNombre() {
        return ResponseEntity.ok(estadoOrdenService.listarOrdenadosPorNombre());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record OrdenesPorEstadoDTO(
            Byte idEstadoOrden,
            String nombreEstado,
            Long cantidadOrdenes
    ) {}

    public record PromedioDiasPorEstadoDTO(
            Byte idEstadoOrden,
            String nombreEstado,
            Double promedioDias
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/estados-orden/estadisticas/ordenes-por-estado
    @GetMapping("/estadisticas/ordenes-por-estado")
    public ResponseEntity<List<OrdenesPorEstadoDTO>> contarOrdenesPorEstado() {
        List<Object[]> resultados = estadoOrdenService.contarOrdenesPorEstado();

        List<OrdenesPorEstadoDTO> respuesta = resultados.stream()
                .map(fila -> new OrdenesPorEstadoDTO(
                        ((Number) fila[0]).byteValue(),                      // idEstadoOrden
                        (String) fila[1],                                     // nombreEstado
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue()// cantidadOrdenes
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/estados-orden/sin-ordenes
    @GetMapping("/sin-ordenes")
    public ResponseEntity<List<EstadoOrden>> listarEstadosSinOrdenes() {
        return ResponseEntity.ok(estadoOrdenService.listarEstadosSinOrdenes());
    }

    // GET /api/estados-orden/estadisticas/promedio-dias
    @GetMapping("/estadisticas/promedio-dias")
    public ResponseEntity<List<PromedioDiasPorEstadoDTO>> obtenerPromedioDiasPorEstado() {
        List<Object[]> resultados = estadoOrdenService.obtenerPromedioDiasPorEstado();

        List<PromedioDiasPorEstadoDTO> respuesta = resultados.stream()
                .map(fila -> new PromedioDiasPorEstadoDTO(
                        ((Number) fila[0]).byteValue(),                        // idEstadoOrden
                        (String) fila[1],                                       // nombreEstado
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()// promedioDias
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/estados-orden/estadisticas/promedio-dias-rango?inicio=2025-01-01&fin=2025-01-31
    @GetMapping("/estadisticas/promedio-dias-rango")
    public ResponseEntity<List<PromedioDiasPorEstadoDTO>> obtenerPromedioDiasPorEstadoEnRango(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados =
                estadoOrdenService.obtenerPromedioDiasPorEstadoEnRango(inicio, fin);

        List<PromedioDiasPorEstadoDTO> respuesta = resultados.stream()
                .map(fila -> new PromedioDiasPorEstadoDTO(
                        ((Number) fila[0]).byteValue(),                        // idEstadoOrden
                        (String) fila[1],                                       // nombreEstado
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()// promedioDias
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
