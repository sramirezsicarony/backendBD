package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.TipoVehiculo;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.TipoVehiculoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200") // Para que Angular pueda consumir el backend
@RestController
@RequestMapping("/api/tipos-vehiculo")
public class TipoVehiculoController {

    private final TipoVehiculoService tipoVehiculoService;

    public TipoVehiculoController(TipoVehiculoService tipoVehiculoService) {
        this.tipoVehiculoService = tipoVehiculoService;
    }

    // ================== CRUD BÁSICO ==================

    // GET /api/tipos-vehiculo
    @GetMapping
    public ResponseEntity<List<TipoVehiculo>> listarTodos() {
        List<TipoVehiculo> tipos = tipoVehiculoService.listarTodos();
        return ResponseEntity.ok(tipos);
    }

    // GET /api/tipos-vehiculo/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TipoVehiculo> obtenerPorId(@PathVariable("id") Integer idTipoVehiculo) {
        TipoVehiculo encontrado = tipoVehiculoService.obtenerPorId(idTipoVehiculo);
        return ResponseEntity.ok(encontrado);
    }

    // POST /api/tipos-vehiculo
    @PostMapping
    public ResponseEntity<TipoVehiculo> crear(@RequestBody TipoVehiculo tipoVehiculo) {
        // Nos aseguramos de que no venga un id manual (lo genera la BD)
        tipoVehiculo.setIdTipoVehiculo(null);
        TipoVehiculo creado = tipoVehiculoService.crear(tipoVehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/tipos-vehiculo/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TipoVehiculo> actualizar(@PathVariable("id") Integer idTipoVehiculo,
                                                   @RequestBody TipoVehiculo tipoActualizado) {
        TipoVehiculo actualizado = tipoVehiculoService.actualizar(idTipoVehiculo, tipoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/tipos-vehiculo/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer idTipoVehiculo) {
        tipoVehiculoService.eliminar(idTipoVehiculo);
        return ResponseEntity.noContent().build();
    }

    // ================== CONSULTAS SIMPLES / INTERMEDIAS ==================

    // GET /api/tipos-vehiculo/por-nombre?tipo=Sedán
    @GetMapping("/por-nombre")
    public ResponseEntity<TipoVehiculo> buscarPorTipoExacto(@RequestParam("tipo") String tipo) {
        TipoVehiculo encontrado = tipoVehiculoService.buscarPorTipoExacto(tipo);
        if (encontrado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(encontrado);
    }

    // GET /api/tipos-vehiculo/buscar?texto=camion
    @GetMapping("/buscar")
    public ResponseEntity<List<TipoVehiculo>> buscarPorTipoConteniendo(@RequestParam("texto") String texto) {
        List<TipoVehiculo> resultados = tipoVehiculoService.buscarPorTipoConteniendo(texto);
        return ResponseEntity.ok(resultados);
    }

    // GET /api/tipos-vehiculo/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public ResponseEntity<List<TipoVehiculo>> buscarPorRangoFechasCreacion(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<TipoVehiculo> resultados = tipoVehiculoService.buscarPorRangoFechasCreacion(inicio, fin);
        return ResponseEntity.ok(resultados);
    }

    // GET /api/tipos-vehiculo/ordenados
    @GetMapping("/ordenados")
    public ResponseEntity<List<TipoVehiculo>> listarOrdenadosPorNombre() {
        List<TipoVehiculo> tiposOrdenados = tipoVehiculoService.listarOrdenadosPorNombre();
        return ResponseEntity.ok(tiposOrdenados);
    }

    // ================== CONSULTAS COMPLEJAS / ESTADÍSTICAS ==================

    // DTO interno para no devolver Object[]
    public record TipoVehiculoEstadisticaDTO(
            Integer idTipoVehiculo,
            String nombreTipo,
            Long cantidadVehiculos
    ) {}

    // GET /api/tipos-vehiculo/estadisticas/por-tipo
    @GetMapping("/estadisticas/por-tipo")
    public ResponseEntity<List<TipoVehiculoEstadisticaDTO>> contarVehiculosPorTipo() {
        List<Object[]> resultados = tipoVehiculoService.contarVehiculosPorTipo();

        List<TipoVehiculoEstadisticaDTO> respuesta = resultados.stream()
                .map(fila -> new TipoVehiculoEstadisticaDTO(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // GET /api/tipos-vehiculo/sin-vehiculos
    @GetMapping("/sin-vehiculos")
    public ResponseEntity<List<TipoVehiculo>> buscarTiposSinVehiculos() {
        List<TipoVehiculo> tipos = tipoVehiculoService.buscarTiposSinVehiculos();
        return ResponseEntity.ok(tipos);
    }

    // GET /api/tipos-vehiculo/estadisticas/por-tipo-rango-anio?inicio=2010&fin=2024
    @GetMapping("/estadisticas/por-tipo-rango-anio")
    public ResponseEntity<List<TipoVehiculoEstadisticaDTO>> contarVehiculosPorTipoEnRangoAnio(
            @RequestParam("inicio") int anioInicio,
            @RequestParam("fin") int anioFin) {

        List<Object[]> resultados = tipoVehiculoService.contarVehiculosPorTipoEnRangoAnio(anioInicio, anioFin);

        List<TipoVehiculoEstadisticaDTO> respuesta = resultados.stream()
                .map(fila -> new TipoVehiculoEstadisticaDTO(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    // ================== MANEJO DE ERRORES ==================

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> manejarEntityNotFoundException(EntityNotFoundException ex) {
        // Devuelve 404 con el mensaje del servicio
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
