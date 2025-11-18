package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.CategoriaRepuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.CategoriaRepuestoService;
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
@RequestMapping("/api/categorias-repuesto")
public class CategoriaRepuestoController {

    private final CategoriaRepuestoService categoriaRepuestoService;

    public CategoriaRepuestoController(CategoriaRepuestoService categoriaRepuestoService) {
        this.categoriaRepuestoService = categoriaRepuestoService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/categorias-repuesto
    @GetMapping
    public ResponseEntity<List<CategoriaRepuesto>> listarTodas() {
        return ResponseEntity.ok(categoriaRepuestoService.listarTodas());
    }

    // GET /api/categorias-repuesto/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaRepuesto> obtenerPorId(@PathVariable("id") Integer idCategoria) {
        CategoriaRepuesto categoria = categoriaRepuestoService.obtenerPorId(idCategoria);
        return ResponseEntity.ok(categoria);
    }

    // POST /api/categorias-repuesto
    @PostMapping
    public ResponseEntity<CategoriaRepuesto> crear(@RequestBody CategoriaRepuesto categoriaRepuesto) {
        // El id lo genera la BD
        categoriaRepuesto.setIdCategoria(null);
        CategoriaRepuesto creada = categoriaRepuestoService.crear(categoriaRepuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/categorias-repuesto/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaRepuesto> actualizar(@PathVariable("id") Integer idCategoria,
                                                        @RequestBody CategoriaRepuesto categoriaRepuestoActualizada) {
        CategoriaRepuesto actualizada = categoriaRepuestoService.actualizar(idCategoria, categoriaRepuestoActualizada);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/categorias-repuesto/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer idCategoria) {
        categoriaRepuestoService.eliminar(idCategoria);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/categorias-repuesto/por-nombre?nombre=Filtros
    @GetMapping("/por-nombre")
    public ResponseEntity<CategoriaRepuesto> obtenerPorNombre(@RequestParam("nombre") String nombreCategoria) {
        CategoriaRepuesto categoria = categoriaRepuestoService.obtenerPorNombre(nombreCategoria);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoria);
    }

    // GET /api/categorias-repuesto/buscar?texto=filt
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaRepuesto>> buscarPorNombreConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(categoriaRepuestoService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/categorias-repuesto/creadas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creadas")
    public ResponseEntity<List<CategoriaRepuesto>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(categoriaRepuestoService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // GET /api/categorias-repuesto/ordenadas
    @GetMapping("/ordenadas")
    public ResponseEntity<List<CategoriaRepuesto>> listarOrdenadasPorNombre() {
        return ResponseEntity.ok(categoriaRepuestoService.listarOrdenadasPorNombre());
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record CategoriaRepuestoConteoDTO(
            Integer idCategoria,
            String nombreCategoria,
            Long cantidadRepuestos
    ) {}

    public record CategoriaRepuestoInventarioDTO(
            Integer idCategoria,
            String nombreCategoria,
            Long stockTotal,
            Double valorTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/categorias-repuesto/estadisticas/repuestos-por-categoria
    @GetMapping("/estadisticas/repuestos-por-categoria")
    public ResponseEntity<List<CategoriaRepuestoConteoDTO>> contarRepuestosPorCategoria() {
        List<Object[]> resultados = categoriaRepuestoService.contarRepuestosPorCategoria();

        List<CategoriaRepuestoConteoDTO> respuesta = resultados.stream()
                .map(fila -> new CategoriaRepuestoConteoDTO(
                        ((Number) fila[0]).intValue(),                         // idCategoria
                        (String) fila[1],                                       // nombreCategoria
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue()   // cantidadRepuestos
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/categorias-repuesto/sin-repuestos
    @GetMapping("/sin-repuestos")
    public ResponseEntity<List<CategoriaRepuesto>> listarCategoriasSinRepuestos() {
        return ResponseEntity.ok(categoriaRepuestoService.listarCategoriasSinRepuestos());
    }

    // GET /api/categorias-repuesto/estadisticas/stock-valor-por-categoria
    @GetMapping("/estadisticas/stock-valor-por-categoria")
    public ResponseEntity<List<CategoriaRepuestoInventarioDTO>> obtenerStockYValorTotalPorCategoriaEnBodega() {
        List<Object[]> resultados = categoriaRepuestoService.obtenerStockYValorTotalPorCategoriaEnBodega();

        List<CategoriaRepuestoInventarioDTO> respuesta = resultados.stream()
                .map(fila -> new CategoriaRepuestoInventarioDTO(
                        ((Number) fila[0]).intValue(),                         // idCategoria
                        (String) fila[1],                                       // nombreCategoria
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(),  // stockTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()// valorTotal
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
