package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.EstadoFactura;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.EstadoFacturaService;
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
@RequestMapping("/api/estados-factura")
public class EstadoFacturaController {

    private final EstadoFacturaService estadoFacturaService;

    public EstadoFacturaController(EstadoFacturaService estadoFacturaService) {
        this.estadoFacturaService = estadoFacturaService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/estados-factura
    @GetMapping
    public ResponseEntity<List<EstadoFactura>> listarTodos() {
        return ResponseEntity.ok(estadoFacturaService.listarTodos());
    }

    // GET /api/estados-factura/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EstadoFactura> obtenerPorId(@PathVariable Byte id) {
        EstadoFactura estado = estadoFacturaService.obtenerPorId(id);
        return ResponseEntity.ok(estado);
    }

    /**
     * POST /api/estados-factura
     *
     * Body ejemplo:
     * {
     *   "estado": "PAGADA"
     * }
     */
    @PostMapping
    public ResponseEntity<EstadoFactura> crear(@RequestBody EstadoFactura estadoFactura) {
        EstadoFactura creado = estadoFacturaService.crear(estadoFactura);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/estados-factura/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EstadoFactura> actualizar(@PathVariable Byte id,
                                                    @RequestBody EstadoFactura estadoActualizado) {
        EstadoFactura actualizado = estadoFacturaService.actualizar(id, estadoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/estados-factura/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Byte id) {
        estadoFacturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/estados-factura/buscar/nombre-exacto?nombre=...
    @GetMapping("/buscar/nombre-exacto")
    public ResponseEntity<EstadoFactura> obtenerPorNombre(
            @RequestParam("nombre") String nombreEstado) {

        EstadoFactura estado = estadoFacturaService.obtenerPorNombre(nombreEstado);
        if (estado == null) {
            throw new EntityNotFoundException(
                    "No se encontró un estado de factura con nombre: " + nombreEstado
            );
        }
        return ResponseEntity.ok(estado);
    }

    // GET /api/estados-factura/buscar/nombre-contiene?texto=...
    @GetMapping("/buscar/nombre-contiene")
    public ResponseEntity<List<EstadoFactura>> buscarPorNombreConteniendo(
            @RequestParam("texto") String texto) {

        return ResponseEntity.ok(estadoFacturaService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/estados-factura/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<EstadoFactura>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(estadoFacturaService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/estados-factura/ordenados/nombre
    @GetMapping("/ordenados/nombre")
    public ResponseEntity<List<EstadoFactura>> listarOrdenadosPorNombre() {
        return ResponseEntity.ok(estadoFacturaService.listarOrdenadosPorNombre());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record FacturasPorEstadoDTO(
            Byte idEstadoFactura,
            String nombreEstado,
            Long cantidadFacturas
    ) {}

    public record TotalFacturadoPorEstadoDTO(
            Byte idEstadoFactura,
            String nombreEstado,
            Double totalFacturado
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/estados-factura/estadisticas/facturas-por-estado
    @GetMapping("/estadisticas/facturas-por-estado")
    public ResponseEntity<List<FacturasPorEstadoDTO>> contarFacturasPorEstado() {
        List<Object[]> resultados = estadoFacturaService.contarFacturasPorEstado();

        List<FacturasPorEstadoDTO> respuesta = resultados.stream()
                .map(fila -> new FacturasPorEstadoDTO(
                        ((Number) fila[0]).byteValue(),                      // idEstadoFactura
                        (String) fila[1],                                     // nombreEstado
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue()// cantidadFacturas
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/estados-factura/sin-facturas
    @GetMapping("/sin-facturas")
    public ResponseEntity<List<EstadoFactura>> listarEstadosSinFacturas() {
        return ResponseEntity.ok(estadoFacturaService.listarEstadosSinFacturas());
    }

    // GET /api/estados-factura/estadisticas/total-facturado?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/estadisticas/total-facturado")
    public ResponseEntity<List<TotalFacturadoPorEstadoDTO>> obtenerTotalFacturadoPorEstadoEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados =
                estadoFacturaService.obtenerTotalFacturadoPorEstadoEnRangoFechas(inicio, fin);

        List<TotalFacturadoPorEstadoDTO> respuesta = resultados.stream()
                .map(fila -> new TotalFacturadoPorEstadoDTO(
                        ((Number) fila[0]).byteValue(),                        // idEstadoFactura
                        (String) fila[1],                                       // nombreEstado
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()// totalFacturado
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
