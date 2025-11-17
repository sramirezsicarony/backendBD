package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Vehiculo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.VehiculoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/vehiculos
    @GetMapping
    public List<Vehiculo> listarTodos() {
        return vehiculoService.listarTodos();
    }

    // GET /api/vehiculos/{idVehiculo}
    @GetMapping("/{idVehiculo}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable String idVehiculo) {
        try {
            Vehiculo vehiculo = vehiculoService.obtenerPorId(idVehiculo);
            return ResponseEntity.ok(vehiculo);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/vehiculos
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        // idVehiculo (placa) debe venir informado; no es autogenerado
        Vehiculo creado = vehiculoService.crear(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/vehiculos/{idVehiculo}
    @PutMapping("/{idVehiculo}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable String idVehiculo,
                                               @RequestBody Vehiculo vehiculoActualizado) {
        try {
            Vehiculo actualizado = vehiculoService.actualizar(idVehiculo, vehiculoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/vehiculos/{idVehiculo}
    @DeleteMapping("/{idVehiculo}")
    public ResponseEntity<Void> eliminar(@PathVariable String idVehiculo) {
        try {
            vehiculoService.eliminar(idVehiculo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== CONSULTAS / FILTROS ======================

    // GET /api/vehiculos/cliente/{idCliente}
    @GetMapping("/cliente/{idCliente}")
    public List<Vehiculo> listarPorIdCliente(@PathVariable String idCliente) {
        return vehiculoService.listarPorIdCliente(idCliente);
    }

    // GET /api/vehiculos/buscar-marca?marca=toyota
    @GetMapping("/buscar-marca")
    public List<Vehiculo> buscarPorMarcaConteniendo(@RequestParam String marca) {
        return vehiculoService.buscarPorMarcaConteniendo(marca);
    }

    // GET /api/vehiculos/buscar-modelo?modelo=corolla
    @GetMapping("/buscar-modelo")
    public List<Vehiculo> buscarPorModeloConteniendo(@RequestParam String modelo) {
        return vehiculoService.buscarPorModeloConteniendo(modelo);
    }

    // GET /api/vehiculos/anio?rangoInicio=2015&rangoFin=2020
    @GetMapping("/anio")
    public List<Vehiculo> buscarPorRangoAnio(@RequestParam("rangoInicio") Short anioInicio,
                                             @RequestParam("rangoFin") Short anioFin) {
        return vehiculoService.buscarPorRangoAnio(anioInicio, anioFin);
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/vehiculos/ordenes
    // Cada Object[]: [idVehiculo, marca, modelo, anio, cantidadOrdenes]
    @GetMapping("/ordenes")
    public List<Object[]> contarOrdenesPorVehiculo() {
        return vehiculoService.contarOrdenesPorVehiculo();
    }

    // GET /api/vehiculos/sin-ordenes
    @GetMapping("/sin-ordenes")
    public List<Vehiculo> listarVehiculosSinOrdenes() {
        return vehiculoService.listarVehiculosSinOrdenes();
    }

    // GET /api/vehiculos/facturacion?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]: [idVehiculo, marca, modelo, anio, totalFacturado, cantidadFacturas]
    @GetMapping("/facturacion")
    public List<Object[]> obtenerTotalFacturadoPorVehiculoEnRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio); // yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return vehiculoService.obtenerTotalFacturadoPorVehiculoEnRangoFechas(fechaInicio, fechaFin);
    }
}
