package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.OrdenTrabajoMecanicoId;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.OrdenTrabajoMecanicoService;
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
@RequestMapping("/api/orden-trabajo-mecanico")
public class OrdenTrabajoMecanicoController {

    private final OrdenTrabajoMecanicoService ordenTrabajoMecanicoService;

    public OrdenTrabajoMecanicoController(OrdenTrabajoMecanicoService ordenTrabajoMecanicoService) {
        this.ordenTrabajoMecanicoService = ordenTrabajoMecanicoService;
    }

    // ================== Helper para ID compuesto ==================

    private OrdenTrabajoMecanicoId buildId(Integer idOrdenTrabajo, String idMecanico) {
        return new OrdenTrabajoMecanicoId(idOrdenTrabajo, idMecanico);
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/orden-trabajo-mecanico
    @GetMapping
    public ResponseEntity<List<OrdenTrabajoMecanico>> listarTodos() {
        return ResponseEntity.ok(ordenTrabajoMecanicoService.listarTodos());
    }

    // GET /api/orden-trabajo-mecanico/{idOrdenTrabajo}/{idMecanico}
    @GetMapping("/{idOrdenTrabajo}/{idMecanico}")
    public ResponseEntity<OrdenTrabajoMecanico> obtenerPorId(@PathVariable Integer idOrdenTrabajo,
                                                             @PathVariable String idMecanico) {
        OrdenTrabajoMecanicoId id = buildId(idOrdenTrabajo, idMecanico);
        OrdenTrabajoMecanico entidad = ordenTrabajoMecanicoService.obtenerPorId(id);
        return ResponseEntity.ok(entidad);
    }

    /**
     * POST /api/orden-trabajo-mecanico
     *
     * Body ejemplo:
     * {
     *   "ordenTrabajo": { "idOrdenTrabajo": 1 },
     *   "mecanico": { "idMecanico": "123456789" },
     *   "rol": { "idRol": 1 },
     *   "horas": 5.5,
     *   "costoHora": 45000.00,
     *   "costoTotal": 247500.00
     * }
     */
    @PostMapping
    public ResponseEntity<OrdenTrabajoMecanico> crear(@RequestBody OrdenTrabajoMecanico entidad) {
        OrdenTrabajoMecanico creado = ordenTrabajoMecanicoService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/orden-trabajo-mecanico/{idOrdenTrabajo}/{idMecanico}
    @PutMapping("/{idOrdenTrabajo}/{idMecanico}")
    public ResponseEntity<OrdenTrabajoMecanico> actualizar(@PathVariable Integer idOrdenTrabajo,
                                                           @PathVariable String idMecanico,
                                                           @RequestBody OrdenTrabajoMecanico entidadActualizada) {
        OrdenTrabajoMecanicoId id = buildId(idOrdenTrabajo, idMecanico);
        OrdenTrabajoMecanico actualizado = ordenTrabajoMecanicoService.actualizar(id, entidadActualizada);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/orden-trabajo-mecanico/{idOrdenTrabajo}/{idMecanico}
    @DeleteMapping("/{idOrdenTrabajo}/{idMecanico}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idOrdenTrabajo,
                                         @PathVariable String idMecanico) {
        OrdenTrabajoMecanicoId id = buildId(idOrdenTrabajo, idMecanico);
        ordenTrabajoMecanicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/orden-trabajo-mecanico/por-orden/{idOrdenTrabajo}
    @GetMapping("/por-orden/{idOrdenTrabajo}")
    public ResponseEntity<List<OrdenTrabajoMecanico>> listarPorIdOrdenTrabajo(
            @PathVariable Integer idOrdenTrabajo) {

        return ResponseEntity.ok(
                ordenTrabajoMecanicoService.listarPorIdOrdenTrabajo(idOrdenTrabajo)
        );
    }

    // GET /api/orden-trabajo-mecanico/por-mecanico/{idMecanico}
    @GetMapping("/por-mecanico/{idMecanico}")
    public ResponseEntity<List<OrdenTrabajoMecanico>> listarPorIdMecanico(
            @PathVariable String idMecanico) {

        return ResponseEntity.ok(
                ordenTrabajoMecanicoService.listarPorIdMecanico(idMecanico)
        );
    }

    // GET /api/orden-trabajo-mecanico/por-rol/{idRol}
    @GetMapping("/por-rol/{idRol}")
    public ResponseEntity<List<OrdenTrabajoMecanico>> listarPorIdRol(
            @PathVariable Byte idRol) {

        return ResponseEntity.ok(
                ordenTrabajoMecanicoService.listarPorIdRol(idRol)
        );
    }

    // GET /api/orden-trabajo-mecanico/buscar/horas?min=1.0&max=8.0
    @GetMapping("/buscar/horas")
    public ResponseEntity<List<OrdenTrabajoMecanico>> buscarPorRangoHoras(
            @RequestParam("min") BigDecimal horasMin,
            @RequestParam("max") BigDecimal horasMax) {

        return ResponseEntity.ok(
                ordenTrabajoMecanicoService.buscarPorRangoHoras(horasMin, horasMax)
        );
    }

    // GET /api/orden-trabajo-mecanico/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<OrdenTrabajoMecanico>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(
                ordenTrabajoMecanicoService.buscarPorRangoFechasCreacion(inicio, fin)
        );
    }

    // ================== DTOs PARA ESTADÍSTICAS ==================

    public record HorasCostoPorMecanicoDTO(
            String idMecanico,
            Double horasTotales,
            Double costoTotal
    ) {}

    public record HorasCostoPorOrdenDTO(
            Integer idOrdenTrabajo,
            Double horasTotales,
            Double costoTotal
    ) {}

    public record HorasCostoPorRolDTO(
            Byte idRol,
            String nombreRol,
            Double horasTotales,
            Double costoTotal
    ) {}

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // GET /api/orden-trabajo-mecanico/estadisticas/horas-costo-por-mecanico?inicio=...&fin=...
    @GetMapping("/estadisticas/horas-costo-por-mecanico")
    public ResponseEntity<List<HorasCostoPorMecanicoDTO>> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados =
                ordenTrabajoMecanicoService.obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(inicio, fin);

        List<HorasCostoPorMecanicoDTO> respuesta = resultados.stream()
                .map(fila -> new HorasCostoPorMecanicoDTO(
                        (String) fila[0],                                      // idMecanico
                        fila[1] == null ? 0.0 : ((Number) fila[1]).doubleValue(), // horasTotales
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()  // costoTotal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/orden-trabajo-mecanico/estadisticas/horas-costo-por-orden
    @GetMapping("/estadisticas/horas-costo-por-orden")
    public ResponseEntity<List<HorasCostoPorOrdenDTO>> obtenerHorasYCostoTotalPorOrden() {
        List<Object[]> resultados = ordenTrabajoMecanicoService.obtenerHorasYCostoTotalPorOrden();

        List<HorasCostoPorOrdenDTO> respuesta = resultados.stream()
                .map(fila -> new HorasCostoPorOrdenDTO(
                        ((Number) fila[0]).intValue(),                          // idOrdenTrabajo
                        fila[1] == null ? 0.0 : ((Number) fila[1]).doubleValue(), // horasTotales
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue()  // costoTotal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/orden-trabajo-mecanico/estadisticas/horas-costo-por-rol?inicio=...&fin=...
    @GetMapping("/estadisticas/horas-costo-por-rol")
    public ResponseEntity<List<HorasCostoPorRolDTO>> obtenerHorasYCostoTotalPorRolEnRangoFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Object[]> resultados =
                ordenTrabajoMecanicoService.obtenerHorasYCostoTotalPorRolEnRangoFechas(inicio, fin);

        List<HorasCostoPorRolDTO> respuesta = resultados.stream()
                .map(fila -> new HorasCostoPorRolDTO(
                        ((Number) fila[0]).byteValue(),                         // idRol
                        (String) fila[1],                                        // nombreRol
                        fila[2] == null ? 0.0 : ((Number) fila[2]).doubleValue(), // horasTotales
                        fila[3] == null ? 0.0 : ((Number) fila[3]).doubleValue()  // costoTotal
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
