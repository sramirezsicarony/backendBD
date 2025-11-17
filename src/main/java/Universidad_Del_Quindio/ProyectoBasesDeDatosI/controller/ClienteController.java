package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Cliente;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/clientes
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    // GET /api/clientes/{idCliente}
    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable String idCliente) {
        try {
            Cliente cliente = clienteService.obtenerPorId(idCliente);
            return ResponseEntity.ok(cliente);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/clientes
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        // idCliente debe venir informado (no es autogenerado)
        Cliente creado = clienteService.crear(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/clientes/{idCliente}
    @PutMapping("/{idCliente}")
    public ResponseEntity<Cliente> actualizar(@PathVariable String idCliente,
                                              @RequestBody Cliente clienteActualizado) {
        try {
            Cliente actualizado = clienteService.actualizar(idCliente, clienteActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/clientes/{idCliente}
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> eliminar(@PathVariable String idCliente) {
        try {
            clienteService.eliminar(idCliente);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== CONSULTAS / FILTROS ======================

    // GET /api/clientes/correo?correo=alguien@ejemplo.com
    @GetMapping("/correo")
    public ResponseEntity<Cliente> obtenerPorCorreo(@RequestParam String correo) {
        Cliente cliente = clienteService.obtenerPorCorreo(correo);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/clientes/buscar-nombre?texto=juan
    @GetMapping("/buscar-nombre")
    public List<Cliente> buscarPorNombreConteniendo(@RequestParam String texto) {
        return clienteService.buscarPorNombreConteniendo(texto);
    }

    // GET /api/clientes/buscar-telefono?prefijo=300
    @GetMapping("/buscar-telefono")
    public List<Cliente> buscarPorTelefonoConPrefijo(@RequestParam String prefijo) {
        return clienteService.buscarPorTelefonoConPrefijo(prefijo);
    }

    // GET /api/clientes/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public List<Cliente> buscarPorRangoFechasCreacion(@RequestParam String inicio,
                                                      @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio); // yyyy-MM-ddTHH:mm:ss
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return clienteService.buscarPorRangoFechasCreacion(fechaInicio, fechaFin);
    }

    // ====================== REPORTES / AGREGADOS ======================

    // GET /api/clientes/vehiculos
    // Cada Object[]: [idCliente, nombreCliente, cantidadVehiculos]
    @GetMapping("/vehiculos")
    public List<Object[]> contarVehiculosPorCliente() {
        return clienteService.contarVehiculosPorCliente();
    }

    // GET /api/clientes/sin-vehiculos
    @GetMapping("/sin-vehiculos")
    public List<Cliente> listarClientesSinVehiculos() {
        return clienteService.listarClientesSinVehiculos();
    }

    // GET /api/clientes/resumen-facturacion?inicio=2025-01-01&fin=2025-12-31
    // Cada Object[]:
    // [idCliente, nombreCliente, totalFacturado,
    //  manoObraTotal, repuestosTotal, impuestoTotal]
    @GetMapping("/resumen-facturacion")
    public List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio); // yyyy-MM-dd
        LocalDate fechaFin = LocalDate.parse(fin);
        return clienteService.obtenerResumenFacturacionPorClienteEnRangoFechas(fechaInicio, fechaFin);
    }
}
