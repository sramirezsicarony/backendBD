package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Repuesto;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.RepuestoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/repuestos
    @GetMapping
    public List<Repuesto> listarTodos() {
        return repuestoService.listarTodos();
    }

    // GET /api/repuestos/{idRepuesto}
    @GetMapping("/{idRepuesto}")
    public ResponseEntity<Repuesto> obtenerPorId(@PathVariable Integer idRepuesto) {
        try {
            Repuesto repuesto = repuestoService.obtenerPorId(idRepuesto);
            return ResponseEntity.ok(repuesto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/repuestos
    @PostMapping
    public ResponseEntity<Repuesto> crear(@RequestBody Repuesto repuesto) {
        Repuesto creado = repuestoService.crear(repuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/repuestos/{idRepuesto}
    @PutMapping("/{idRepuesto}")
    public ResponseEntity<Repuesto> actualizar(@PathVariable Integer idRepuesto,
                                               @RequestBody Repuesto repuestoActualizado) {
        try {
            Repuesto actualizado = repuestoService.actualizar(idRepuesto, repuestoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/repuestos/{idRepuesto}
    @DeleteMapping("/{idRepuesto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idRepuesto) {
        try {
            repuestoService.eliminar(idRepuesto);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== BÚSQUEDAS / CONSULTAS ======================

    // GET /api/repuestos/nombre?nombre=FiltroAceite
    @GetMapping("/nombre")
    public ResponseEntity<Repuesto> obtenerPorNombre(@RequestParam String nombre) {
        Repuesto repuesto = repuestoService.obtenerPorNombre(nombre);
        if (repuesto != null) {
            return ResponseEntity.ok(repuesto);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/repuestos/buscar?texto=filtro
    @GetMapping("/buscar")
    public List<Repuesto> buscarPorNombreConteniendo(@RequestParam String texto) {
        return repuestoService.buscarPorNombreConteniendo(texto);
    }

    // GET /api/repuestos/categoria/{idCategoria}
    @GetMapping("/categoria/{idCategoria}")
    public List<Repuesto> listarPorCategoria(@PathVariable Integer idCategoria) {
        return repuestoService.listarPorIdCategoria(idCategoria);
    }

    // GET /api/repuestos/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public List<Repuesto> buscarPorRangoFechasCreacion(@RequestParam String inicio,
                                                       @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio); // formato ISO: yyyy-MM-ddTHH:mm:ss
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return repuestoService.buscarPorRangoFechasCreacion(fechaInicio, fechaFin);
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/repuestos/stock-valor
    // Cada Object[]: [idRepuesto, nombreRepuesto, stockTotal, valorTotal]
    @GetMapping("/stock-valor")
    public List<Object[]> obtenerStockYValorTotalPorRepuesto() {
        return repuestoService.obtenerStockYValorTotalPorRepuesto();
    }

    // GET /api/repuestos/ventas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    // Cada Object[]: [idRepuesto, nombreRepuesto, cantidadTotal, totalVendido]
    @GetMapping("/ventas")
    public List<Object[]> obtenerVentasPorRepuestoEnRangoFechas(@RequestParam String inicio,
                                                                @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return repuestoService.obtenerVentasPorRepuestoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/repuestos/compras?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idRepuesto, nombreRepuesto, cantidadComprada, costoTotalCompras]
    @GetMapping("/compras")
    public List<Object[]> obtenerComprasPorRepuestoEnRangoFechas(@RequestParam String inicio,
                                                                 @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio); // yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return repuestoService.obtenerComprasPorRepuestoEnRangoFechas(fechaInicio, fechaFin);
    }
}
