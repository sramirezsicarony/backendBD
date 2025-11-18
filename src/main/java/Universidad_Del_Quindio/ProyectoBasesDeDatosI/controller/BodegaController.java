package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Bodega;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.BodegaId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.BodegaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/bodega")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/bodega
    @GetMapping
    public ResponseEntity<List<Bodega>> listarTodos() {
        return ResponseEntity.ok(bodegaService.listarTodos());
    }

    // GET /api/bodega/{idAlmacen}/{idRepuesto}
    @GetMapping("/{idAlmacen}/{idRepuesto}")
    public ResponseEntity<Bodega> obtenerPorId(@PathVariable Integer idAlmacen,
                                               @PathVariable Integer idRepuesto) {
        BodegaId id = new BodegaId(idAlmacen, idRepuesto);
        Bodega bodega = bodegaService.obtenerPorId(id);
        return ResponseEntity.ok(bodega);
    }

    /**
     * POST /api/bodega
     *
     * Body esperado (ejemplo):
     * {
     *   "almacen": { "idAlmacen": 1 },
     *   "repuesto": { "idRepuesto": 10 },
     *   "stock": 50,
     *   "precioVenta": 123000.50
     * }
     *
     * El BodegaId se construye en el Service si viene null.
     */
    @PostMapping
    public ResponseEntity<Bodega> crear(@RequestBody Bodega bodega) {
        Bodega creada = bodegaService.crear(bodega);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/bodega/{idAlmacen}/{idRepuesto}
    @PutMapping("/{idAlmacen}/{idRepuesto}")
    public ResponseEntity<Bodega> actualizar(@PathVariable Integer idAlmacen,
                                             @PathVariable Integer idRepuesto,
                                             @RequestBody Bodega bodegaActualizada) {
        BodegaId id = new BodegaId(idAlmacen, idRepuesto);
        Bodega actualizada = bodegaService.actualizar(id, bodegaActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/bodega/{idAlmacen}/{idRepuesto}
    @DeleteMapping("/{idAlmacen}/{idRepuesto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAlmacen,
                                         @PathVariable Integer idRepuesto) {
        BodegaId id = new BodegaId(idAlmacen, idRepuesto);
        bodegaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/bodega/por-almacen/{idAlmacen}
    @GetMapping("/por-almacen/{idAlmacen}")
    public ResponseEntity<List<Bodega>> listarPorIdAlmacen(@PathVariable Integer idAlmacen) {
        return ResponseEntity.ok(bodegaService.listarPorIdAlmacen(idAlmacen));
    }

    // GET /api/bodega/por-stock-minimo?stockMinimo=10
    @GetMapping("/por-stock-minimo")
    public ResponseEntity<List<Bodega>> buscarPorStockMayorIgual(
            @RequestParam("stockMinimo") Integer stockMinimo) {
        return ResponseEntity.ok(bodegaService.buscarPorStockMayorIgual(stockMinimo));
    }

    // GET /api/bodega/por-rango-precio?min=10000&max=50000
    @GetMapping("/por-rango-precio")
    public ResponseEntity<List<Bodega>> buscarPorRangoPrecioVenta(
            @RequestParam("min") BigDecimal precioMin,
            @RequestParam("max") BigDecimal precioMax) {

        return ResponseEntity.ok(bodegaService.buscarPorRangoPrecioVenta(precioMin, precioMax));
    }

    // GET /api/bodega/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<Bodega>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(bodegaService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record StockPorAlmacenDTO(
            Integer idAlmacen,
            String nombreAlmacen,
            Long stockTotal,
            Double valorTotal
    ) {}

    public record StockPorRepuestoDTO(
            Integer idRepuesto,
            String nombreRepuesto,
            Long stockTotal,
            Double valorTotal
    ) {}

    public record RepuestoStockBajoDTO(
            Integer idRepuesto,
            String nombreRepuesto,
            Long stockTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/bodega/estadisticas/por-almacen
    @GetMapping("/estadisticas/por-almacen")
    public ResponseEntity<List<StockPorAlmacenDTO>> obtenerStockYValorTotalPorAlmacen() {
        List<Object[]> resultados = bodegaService.obtenerStockYValorTotalPorAlmacen();

        List<StockPorAlmacenDTO> respuesta = resultados.stream()
                .map(fila -> new StockPorAlmacenDTO(
                        ((Number) fila[0]).intValue(),                         // idAlmacen
                        (String) fila[1],                                       // nombreAlmacen
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),  // stockTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()// valorTotal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/bodega/estadisticas/por-repuesto-global
    @GetMapping("/estadisticas/por-repuesto-global")
    public ResponseEntity<List<StockPorRepuestoDTO>> obtenerStockYValorTotalPorRepuestoGlobal() {
        List<Object[]> resultados = bodegaService.obtenerStockYValorTotalPorRepuestoGlobal();

        List<StockPorRepuestoDTO> respuesta = resultados.stream()
                .map(fila -> new StockPorRepuestoDTO(
                        ((Number) fila[0]).intValue(),                         // idRepuesto
                        (String) fila[1],                                       // nombreRepuesto
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),  // stockTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()// valorTotal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/bodega/estadisticas/repuestos-stock-bajo?umbral=20
    @GetMapping("/estadisticas/repuestos-stock-bajo")
    public ResponseEntity<List<RepuestoStockBajoDTO>> obtenerRepuestosConStockGlobalBajo(
            @RequestParam("umbral") Integer umbral) {

        List<Object[]> resultados = bodegaService.obtenerRepuestosConStockGlobalBajo(umbral);

        List<RepuestoStockBajoDTO> respuesta = resultados.stream()
                .map(fila -> new RepuestoStockBajoDTO(
                        ((Number) fila[0]).intValue(),                         // idRepuesto
                        (String) fila[1],                                       // nombreRepuesto
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue()   // stockTotal
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
