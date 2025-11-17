package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Mecanico;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.MecanicoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mecanicos")
public class MecanicoController {

    private final MecanicoService mecanicoService;

    public MecanicoController(MecanicoService mecanicoService) {
        this.mecanicoService = mecanicoService;
    }

    // ====================== CRUD BÁSICO ======================

    // GET /api/mecanicos
    @GetMapping
    public List<Mecanico> listarTodos() {
        return mecanicoService.listarTodos();
    }

    // GET /api/mecanicos/{idMecanico}
    @GetMapping("/{idMecanico}")
    public ResponseEntity<Mecanico> obtenerPorId(@PathVariable String idMecanico) {
        try {
            Mecanico mecanico = mecanicoService.obtenerPorId(idMecanico);
            return ResponseEntity.ok(mecanico);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/mecanicos
    @PostMapping
    public ResponseEntity<Mecanico> crear(@RequestBody Mecanico mecanico) {
        // idMecanico debe venir en el body (no es autogenerado)
        Mecanico creado = mecanicoService.crear(mecanico);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/mecanicos/{idMecanico}
    @PutMapping("/{idMecanico}")
    public ResponseEntity<Mecanico> actualizar(@PathVariable String idMecanico,
                                               @RequestBody Mecanico mecanicoActualizado) {
        try {
            Mecanico actualizado = mecanicoService.actualizar(idMecanico, mecanicoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/mecanicos/{idMecanico}
    @DeleteMapping("/{idMecanico}")
    public ResponseEntity<Void> eliminar(@PathVariable String idMecanico) {
        try {
            mecanicoService.eliminar(idMecanico);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ====================== CONSULTAS / FILTROS ======================

    // GET /api/mecanicos/especialidad/{idEspecialidad}
    @GetMapping("/especialidad/{idEspecialidad}")
    public List<Mecanico> listarPorIdEspecialidad(@PathVariable Integer idEspecialidad) {
        return mecanicoService.listarPorIdEspecialidad(idEspecialidad);
    }

    // GET /api/mecanicos/experiencia?min=3
    @GetMapping("/experiencia")
    public List<Mecanico> buscarPorExperienciaMayorIgual(@RequestParam("min") Byte experiencia) {
        return mecanicoService.buscarPorExperienciaMayorIgual(experiencia);
    }

    // GET /api/mecanicos/costo-hora?rangoMin=20000&rangoMax=50000
    @GetMapping("/costo-hora")
    public List<Mecanico> buscarPorRangoCostoHora(@RequestParam("rangoMin") BigDecimal costoMin,
                                                  @RequestParam("rangoMax") BigDecimal costoMax) {
        return mecanicoService.buscarPorRangoCostoHora(costoMin, costoMax);
    }

    // GET /api/mecanicos/creados?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    @GetMapping("/creados")
    public List<Mecanico> buscarPorRangoFechasCreacion(@RequestParam String inicio,
                                                       @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio); // yyyy-MM-ddTHH:mm:ss
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return mecanicoService.buscarPorRangoFechasCreacion(fechaInicio, fechaFin);
    }

    // ====================== REPORTES / RENDIMIENTO ======================

    // GET /api/mecanicos/horas-costo?rangoInicio=2025-01-01T00:00:00&rangoFin=2025-12-31T23:59:59
    // Cada Object[]: [idMecanico, experiencia, horasTotales, costoTotal]
    @GetMapping("/horas-costo")
    public List<Object[]> obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(
            @RequestParam("rangoInicio") String inicio,
            @RequestParam("rangoFin") String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return mecanicoService.obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/mecanicos/sin-ordenes
    @GetMapping("/sin-ordenes")
    public List<Mecanico> listarMecanicosSinOrdenes() {
        return mecanicoService.listarMecanicosSinOrdenes();
    }

    // GET /api/mecanicos/horas-por-area?rangoInicio=2025-01-01T00:00:00&rangoFin=2025-12-31T23:59:59
    // Cada Object[]: [idMecanico, idAreaLaboral, nombreArea, horasTotales]
    @GetMapping("/horas-por-area")
    public List<Object[]> obtenerHorasPorMecanicoYAreaEnRangoFechas(
            @RequestParam("rangoInicio") String inicio,
            @RequestParam("rangoFin") String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return mecanicoService.obtenerHorasPorMecanicoYAreaEnRangoFechas(fechaInicio, fechaFin);
    }
}
