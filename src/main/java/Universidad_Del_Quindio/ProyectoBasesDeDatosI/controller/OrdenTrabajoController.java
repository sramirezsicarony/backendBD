package Universidad_Del_Quindio.ProyectoBasesDeDatosI.controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.OrdenTrabajoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-trabajo")
public class OrdenTrabajoController {

    private final OrdenTrabajoService ordenTrabajoService;

    public OrdenTrabajoController(OrdenTrabajoService ordenTrabajoService) {
        this.ordenTrabajoService = ordenTrabajoService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/ordenes-trabajo
    @GetMapping
    public List<OrdenTrabajo> listarTodas() {
        return ordenTrabajoService.listarTodas();
    }

    // GET /api/ordenes-trabajo/{idOrdenTrabajo}
    @GetMapping("/{idOrdenTrabajo}")
    public ResponseEntity<OrdenTrabajo> obtenerPorId(@PathVariable Integer idOrdenTrabajo) {
        try {
            OrdenTrabajo orden = ordenTrabajoService.obtenerPorId(idOrdenTrabajo);
            return ResponseEntity.ok(orden);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/ordenes-trabajo
    @PostMapping
    public ResponseEntity<OrdenTrabajo> crear(@RequestBody OrdenTrabajo ordenTrabajo) {
        OrdenTrabajo creada = ordenTrabajoService.crear(ordenTrabajo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/ordenes-trabajo/{idOrdenTrabajo}
    @PutMapping("/{idOrdenTrabajo}")
    public ResponseEntity<OrdenTrabajo> actualizar(@PathVariable Integer idOrdenTrabajo,
                                                   @RequestBody OrdenTrabajo ordenTrabajoActualizada) {
        try {
            OrdenTrabajo actualizada = ordenTrabajoService.actualizar(idOrdenTrabajo, ordenTrabajoActualizada);
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/ordenes-trabajo/{idOrdenTrabajo}
    @DeleteMapping("/{idOrdenTrabajo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idOrdenTrabajo) {
        try {
            ordenTrabajoService.eliminar(idOrdenTrabajo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== CONSULTAS / FILTROS ======================

    // GET /api/ordenes-trabajo/vehiculo/{idVehiculo}
    @GetMapping("/vehiculo/{idVehiculo}")
    public List<OrdenTrabajo> listarPorIdVehiculo(@PathVariable String idVehiculo) {
        return ordenTrabajoService.listarPorIdVehiculo(idVehiculo);
    }

    // GET /api/ordenes-trabajo/estado/{idEstadoOrden}
    @GetMapping("/estado/{idEstadoOrden}")
    public List<OrdenTrabajo> listarPorIdEstadoOrden(@PathVariable Byte idEstadoOrden) {
        return ordenTrabajoService.listarPorIdEstadoOrden(idEstadoOrden);
    }

    // GET /api/ordenes-trabajo/fecha-ingreso?inicio=2025-01-01&fin=2025-12-31
    @GetMapping("/fecha-ingreso")
    public List<OrdenTrabajo> buscarPorRangoFechaIngreso(@RequestParam String inicio,
                                                         @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio); // yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return ordenTrabajoService.buscarPorRangoFechaIngreso(fechaInicio, fechaFin);
    }

    // GET /api/ordenes-trabajo/abiertas
    @GetMapping("/abiertas")
    public List<OrdenTrabajo> listarOrdenesAbiertas() {
        return ordenTrabajoService.listarOrdenesAbiertas();
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/ordenes-trabajo/por-estado
    // Cada Object[]: [idEstadoOrden, nombreEstado, cantidadOrdenes]
    @GetMapping("/por-estado")
    public List<Object[]> contarOrdenesPorEstado() {
        return ordenTrabajoService.contarOrdenesPorEstado();
    }

    // GET /api/ordenes-trabajo/por-cliente?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idCliente, nombreCliente, cantidadOrdenes]
    @GetMapping("/por-cliente")
    public List<Object[]> contarOrdenesPorClienteEnRangoFechas(@RequestParam String inicio,
                                                               @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return ordenTrabajoService.contarOrdenesPorClienteEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/ordenes-trabajo/resumen-costos?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]:
    // [idOrdenTrabajo, fechaIngreso, totalFacturado, manoObraTotal,
    //  repuestosTotal, impuestoTotal, cantidadFacturas]
    @GetMapping("/resumen-costos")
    public List<Object[]> obtenerResumenCostosPorOrdenEnRangoFechas(@RequestParam String inicio,
                                                                    @RequestParam String fin) {
        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return ordenTrabajoService.obtenerResumenCostosPorOrdenEnRangoFechas(fechaInicio, fechaFin);
    }
}
