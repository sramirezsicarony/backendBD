package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Almacen;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.AlmacenService;
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
@RequestMapping("/api/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/almacenes
    @GetMapping
    public ResponseEntity<List<Almacen>> listarTodos() {
        return ResponseEntity.ok(almacenService.listarTodos());
    }

    // GET /api/almacenes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerPorId(@PathVariable("id") Integer idAlmacen) {
        Almacen almacen = almacenService.obtenerPorId(idAlmacen);
        return ResponseEntity.ok(almacen);
    }

    // POST /api/almacenes
    @PostMapping
    public ResponseEntity<Almacen> crear(@RequestBody Almacen almacen) {
        // El id lo genera la BD
        almacen.setIdAlmacen(null);
        Almacen creado = almacenService.crear(almacen);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/almacenes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizar(@PathVariable("id") Integer idAlmacen,
                                              @RequestBody Almacen almacenActualizado) {
        Almacen actualizado = almacenService.actualizar(idAlmacen, almacenActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/almacenes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer idAlmacen) {
        almacenService.eliminar(idAlmacen);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/almacenes/por-nombre?nombre=Central
    @GetMapping("/por-nombre")
    public ResponseEntity<Almacen> obtenerPorNombre(@RequestParam("nombre") String nombre) {
        Almacen almacen = almacenService.obtenerPorNombre(nombre);
        if (almacen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(almacen);
    }

    // GET /api/almacenes/buscar-por-nombre?texto=centro
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<Almacen>> buscarPorNombreConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(almacenService.buscarPorNombreConteniendo(texto));
    }

    // GET /api/almacenes/buscar-por-direccion?texto=carrera
    @GetMapping("/buscar-por-direccion")
    public ResponseEntity<List<Almacen>> buscarPorDireccionConteniendo(@RequestParam("texto") String texto) {
        return ResponseEntity.ok(almacenService.buscarPorDireccionConteniendo(texto));
    }

    // GET /api/almacenes/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<Almacen>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(almacenService.buscarPorRangoFechasCreacion(inicio, fin));
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    // Para resumen de repuestos y stock
    public record AlmacenResumenDTO(
            Integer idAlmacen,
            String nombreAlmacen,
            Long cantidadRepuestos,
            Long stockTotal
    ) {}

    // Para stock y valor total de inventario
    public record AlmacenInventarioDTO(
            Integer idAlmacen,
            String nombreAlmacen,
            Long stockTotal,
            Double valorTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/almacenes/estadisticas/resumen-stock
    @GetMapping("/estadisticas/resumen-stock")
    public ResponseEntity<List<AlmacenResumenDTO>> obtenerResumenRepuestosYStockPorAlmacen() {
        List<Object[]> resultados = almacenService.obtenerResumenRepuestosYStockPorAlmacen();

        List<AlmacenResumenDTO> respuesta = resultados.stream()
                .map(fila -> new AlmacenResumenDTO(
                        ((Number) fila[0]).intValue(),   // idAlmacen
                        (String) fila[1],                 // nombreAlmacen
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(), // cantidadRepuestos
                        fila[3] == null ? 0L : ((Number) fila[3]).longValue()  // stockTotal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/almacenes/sin-stock
    @GetMapping("/sin-stock")
    public ResponseEntity<List<Almacen>> listarAlmacenesSinStock() {
        return ResponseEntity.ok(almacenService.listarAlmacenesSinStock());
    }

    // GET /api/almacenes/estadisticas/valor-inventario
    @GetMapping("/estadisticas/valor-inventario")
    public ResponseEntity<List<AlmacenInventarioDTO>> obtenerStockYValorTotalPorAlmacen() {
        List<Object[]> resultados = almacenService.obtenerStockYValorTotalPorAlmacen();

        List<AlmacenInventarioDTO> respuesta = resultados.stream()
                .map(fila -> new AlmacenInventarioDTO(
                        ((Number) fila[0]).intValue(),   // idAlmacen
                        (String) fila[1],                 // nombreAlmacen
                        fila[2] == null ? 0L : ((Number) fila[2]).longValue(), // stockTotal
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue() // valorTotal
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
