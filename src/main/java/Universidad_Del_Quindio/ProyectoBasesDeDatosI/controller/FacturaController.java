package Universidad_Del_Quindio.ProyectoBasesDeDatosI.controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Factura;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.FacturaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/facturas
    @GetMapping
    public List<Factura> listarTodas() {
        return facturaService.listarTodas();
    }

    // GET /api/facturas/{idFactura}
    @GetMapping("/{idFactura}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Integer idFactura) {
        try {
            Factura factura = facturaService.obtenerPorId(idFactura);
            return ResponseEntity.ok(factura);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/facturas
    @PostMapping
    public ResponseEntity<Factura> crear(@RequestBody Factura factura) {
        Factura creada = facturaService.crear(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/facturas/{idFactura}
    @PutMapping("/{idFactura}")
    public ResponseEntity<Factura> actualizar(@PathVariable Integer idFactura,
                                              @RequestBody Factura facturaActualizada) {
        try {
            Factura actualizada = facturaService.actualizar(idFactura, facturaActualizada);
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/facturas/{idFactura}
    @DeleteMapping("/{idFactura}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idFactura) {
        try {
            facturaService.eliminar(idFactura);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== CONSULTAS / FILTROS ======================

    // GET /api/facturas/cliente/{idCliente}
    @GetMapping("/cliente/{idCliente}")
    public List<Factura> listarPorIdCliente(@PathVariable String idCliente) {
        return facturaService.listarPorIdCliente(idCliente);
    }

    // GET /api/facturas/estado/{idEstadoFactura}
    @GetMapping("/estado/{idEstadoFactura}")
    public List<Factura> listarPorIdEstadoFactura(@PathVariable Byte idEstadoFactura) {
        return facturaService.listarPorIdEstadoFactura(idEstadoFactura);
    }

    // GET /api/facturas/fecha?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/fecha")
    public List<Factura> buscarPorFechaCreacionEntre(@RequestParam String inicio,
                                                     @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio); // yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.buscarPorFechaCreacionEntre(fechaInicio, fechaFin);
    }

    // GET /api/facturas/total?rangoMin=100000&rangoMax=500000
    @GetMapping("/total")
    public List<Factura> buscarPorTotalEntre(@RequestParam("rangoMin") BigDecimal totalMin,
                                             @RequestParam("rangoMax") BigDecimal totalMax) {
        return facturaService.buscarPorTotalEntre(totalMin, totalMax);
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/facturas/resumen-cliente?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idCliente, nombreCliente, totalFacturado, manoObraTotal,
    //                 repuestosTotal, impuestoTotal, cantidadFacturas]
    @GetMapping("/resumen-cliente")
    public List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.obtenerResumenFacturacionPorClienteEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/facturas/total-por-estado?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idEstadoFactura, nombreEstado, totalFacturado, cantidadFacturas]
    @GetMapping("/total-por-estado")
    public List<Object[]> obtenerTotalFacturadoPorEstadoEnRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.obtenerTotalFacturadoPorEstadoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/facturas/facturacion-mensual?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [anio, mes, totalFacturado, manoObraTotal,
    //                 repuestosTotal, impuestoTotal, cantidadFacturas]
    @GetMapping("/facturacion-mensual")
    public List<Object[]> obtenerFacturacionMensualEnRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.obtenerFacturacionMensualEnRangoFechas(fechaInicio, fechaFin);
    }
}
