package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Proveedor;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ProveedorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/proveedores
    @GetMapping
    public List<Proveedor> listarTodos() {
        return proveedorService.listarTodos();
    }

    // GET /api/proveedores/{idProveedor}
    @GetMapping("/{idProveedor}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer idProveedor) {
        try {
            Proveedor proveedor = proveedorService.obtenerPorId(idProveedor);
            return ResponseEntity.ok(proveedor);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/proveedores
    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        Proveedor creado = proveedorService.crear(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/proveedores/{idProveedor}
    @PutMapping("/{idProveedor}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Integer idProveedor,
                                                @RequestBody Proveedor proveedorActualizado) {
        try {
            Proveedor actualizado = proveedorService.actualizar(idProveedor, proveedorActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/proveedores/{idProveedor}
    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idProveedor) {
        try {
            proveedorService.eliminar(idProveedor);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== BÚSQUEDAS / FILTROS ======================

    // GET /api/proveedores/nombre?nombre=ProveedorX
    @GetMapping("/nombre")
    public ResponseEntity<Proveedor> obtenerPorNombre(@RequestParam String nombre) {
        Proveedor proveedor = proveedorService.obtenerPorNombre(nombre);
        if (proveedor != null) {
            return ResponseEntity.ok(proveedor);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/proveedores/buscar-nombre?texto=auto
    @GetMapping("/buscar-nombre")
    public List<Proveedor> buscarPorNombreConteniendo(@RequestParam String texto) {
        return proveedorService.buscarPorNombreConteniendo(texto);
    }

    // GET /api/proveedores/buscar-direccion?texto=calle
    @GetMapping("/buscar-direccion")
    public List<Proveedor> buscarPorDireccionConteniendo(@RequestParam String texto) {
        return proveedorService.buscarPorDireccionConteniendo(texto);
    }

    // GET /api/proveedores/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public List<Proveedor> buscarPorRangoFechasCreacion(@RequestParam String inicio,
                                                        @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio); // formato ISO: yyyy-MM-ddTHH:mm:ss
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return proveedorService.buscarPorRangoFechasCreacion(fechaInicio, fechaFin);
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/proveedores/resumen-suministros
    // Cada Object[]: [idProveedor, nombreProveedor, cantidadTotal, costoTotal]
    @GetMapping("/resumen-suministros")
    public List<Object[]> obtenerResumenSuministrosPorProveedor() {
        return proveedorService.obtenerResumenSuministrosPorProveedor();
    }

    // GET /api/proveedores/sin-suministros
    @GetMapping("/sin-suministros")
    public List<Proveedor> listarProveedoresSinSuministros() {
        return proveedorService.listarProveedoresSinSuministros();
    }

    // GET /api/proveedores/costo-total?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idProveedor, nombreProveedor, costoTotal]
    @GetMapping("/costo-total")
    public List<Object[]> obtenerCostoTotalPorProveedorEnRangoFechas(@RequestParam String inicio,
                                                                     @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio); // formato ISO: yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return proveedorService.obtenerCostoTotalPorProveedorEnRangoFechas(fechaInicio, fechaFin);
    }
}
