package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Suministra;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.SuministraId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.SuministraService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/suministros")
public class SuministraController {

    private final SuministraService suministraService;

    public SuministraController(SuministraService suministraService) {
        this.suministraService = suministraService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/suministros
    @GetMapping
    public ResponseEntity<List<Suministra>> listarTodos() {
        return ResponseEntity.ok(suministraService.listarTodos());
    }

    // GET /api/suministros/{idProveedor}/{idRepuesto}
    @GetMapping("/{idProveedor}/{idRepuesto}")
    public ResponseEntity<Suministra> obtenerPorId(@PathVariable Integer idProveedor,
                                                   @PathVariable Integer idRepuesto) {
        SuministraId id = new SuministraId(idProveedor, idRepuesto);
        Suministra suministra = suministraService.obtenerPorId(id);
        return ResponseEntity.ok(suministra);
    }

    /**
     * POST /api/suministros
     *
     * Body ejemplo:
     * {
     *   "proveedor": { "idProveedor": 1 },
     *   "repuesto": { "idRepuesto": 10 },
     *   "costoUnitario": 50000.00,
     *   "cantidad": 20,
     *   "costoTotal": 1000000.00,
     *   "fechaIngreso": "2025-11-17"
     * }
     */
    @PostMapping
    public ResponseEntity<Suministra> crear(@RequestBody Suministra suministra) {
        Suministra creado = suministraService.crear(suministra);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/suministros/{idProveedor}/{idRepuesto}
    @PutMapping("/{idProveedor}/{idRepuesto}")
    public ResponseEntity<Suministra> actualizar(@PathVariable Integer idProveedor,
                                                 @PathVariable Integer idRepuesto,
                                                 @RequestBody Suministra suministraActualizada) {
        SuministraId id = new SuministraId(idProveedor, idRepuesto);
        Suministra actualizado = suministraService.actualizar(id, suministraActualizada);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/suministros/{idProveedor}/{idRepuesto}
    @DeleteMapping("/{idProveedor}/{idRepuesto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idProveedor,
                                         @PathVariable Integer idRepuesto) {
        SuministraId id = new SuministraId(idProveedor, idRepuesto);
        suministraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/suministros/por-proveedor/{idProveedor}
    @GetMapping("/por-proveedor/{idProveedor}")
    public ResponseEntity<List<Suministra>> listarPorIdProveedor(@PathVariable Integer idProveedor) {
        return ResponseEntity.ok(suministraService.listarPorIdProveedor(idProveedor));
    }

    // GET /api/suministros/por-repuesto/{idRepuesto}
    @GetMapping("/por-repuesto/{idRepuesto}")
    public ResponseEntity<List<Suministra>> listarPorIdRepuesto(@PathVariable Integer idRepuesto) {
        return ResponseEntity.ok(suministraService.listarPorIdRepuesto(idRepuesto));
    }

    // GET /api/suministros/por-fecha-ingreso?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/por-fecha-ingreso")
    public ResponseEntity<List<Suministra>> buscarPorRangoFechaIngreso(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        return ResponseEntity.ok(
                suministraService.buscarPorRangoFechaIngreso(inicio, fin)
        );
    }

    // GET /api/suministros/por-costo-unitario?min=10000&max=50000
    @GetMapping("/por-costo-unitario")
    public ResponseEntity<List<Suministra>> buscarPorRangoCostoUnitario(
            @RequestParam("min") BigDecimal costoMin,
            @RequestParam("max") BigDecimal costoMax) {

        return ResponseEntity.ok(
                suministraService.buscarPorRangoCostoUnitario(costoMin, costoMax)
        );
    }

    // GET /api/suministros/por-cantidad?min=50
    @GetMapping("/por-cantidad")
    public ResponseEntity<List<Suministra>> buscarPorCantidadMayorIgual(
            @RequestParam("min") Integer cantidadMinima) {

        return ResponseEntity.ok(
                suministraService.buscarPorCantidadMayorIgual(cantidadMinima)
        );
    }

    // GET /api/suministros/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<Suministra>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(
                suministraService.buscarPorRangoFechasCreacion(inicio, fin)
        );
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record ResumenComprasProveedorDTO(
            Integer idProveedor,
            String nombreProveedor,
            Long cantidadTotal,
            BigDecimal costoTotal
    ) {}

    public record ResumenComprasRepuestoDTO(
            Integer idRepuesto,
            String nombreRepuesto,
            Long cantidadTotal,
            BigDecimal costoTotal
    ) {}

    public record ComprasProveedorRangoDTO(
            Integer idProveedor,
            String nombreProveedor,
            Long cantidadTotal,
            BigDecimal costoTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/suministros/estadisticas/compras-por-proveedor
    @GetMapping("/estadisticas/compras-por-proveedor")
    public ResponseEntity<List<ResumenComprasProveedorDTO>> obtenerResumenComprasPorProveedor() {
        List<Object[]> resultados = suministraService.obtenerResumenComprasPorProveedor();

        List<ResumenComprasProveedorDTO> respuesta = resultados.stream()
                .map(fila -> new ResumenComprasProveedorDTO(
                        ((Number) fila[0]).intValue(),                        // idProveedor
                        (String) fila[1],                                     // nombreProveedor
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),// cantidadTotal
                        fila[3] == null ? BigDecimal.ZERO                     // costoTotal
                                : (fila[3] instanceof BigDecimal
                                ? (BigDecimal) fila[3]
                                : BigDecimal.valueOf(((Number) fila[3]).doubleValue()))
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/suministros/estadisticas/compras-por-repuesto
    @GetMapping("/estadisticas/compras-por-repuesto")
    public ResponseEntity<List<ResumenComprasRepuestoDTO>> obtenerResumenComprasPorRepuesto() {
        List<Object[]> resultados = suministraService.obtenerResumenComprasPorRepuesto();

        List<ResumenComprasRepuestoDTO> respuesta = resultados.stream()
                .map(fila -> new ResumenComprasRepuestoDTO(
                        ((Number) fila[0]).intValue(),                        // idRepuesto
                        (String) fila[1],                                     // nombreRepuesto
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),// cantidadTotal
                        fila[3] == null ? BigDecimal.ZERO                     // costoTotal
                                : (fila[3] instanceof BigDecimal
                                ? (BigDecimal) fila[3]
                                : BigDecimal.valueOf(((Number) fila[3]).doubleValue()))
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/suministros/estadisticas/compras-por-proveedor-rango?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/estadisticas/compras-por-proveedor-rango")
    public ResponseEntity<List<ComprasProveedorRangoDTO>> obtenerComprasPorProveedorEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<Object[]> resultados = suministraService.obtenerComprasPorProveedorEnRangoFechas(inicio, fin);

        List<ComprasProveedorRangoDTO> respuesta = resultados.stream()
                .map(fila -> new ComprasProveedorRangoDTO(
                        ((Number) fila[0]).intValue(),                        // idProveedor
                        (String) fila[1],                                     // nombreProveedor
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),// cantidadTotal
                        fila[3] == null ? BigDecimal.ZERO                     // costoTotal
                                : (fila[3] instanceof BigDecimal
                                ? (BigDecimal) fila[3]
                                : BigDecimal.valueOf(((Number) fila[3]).doubleValue()))
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
