package Universidad_Del_Quindio.ProyectoBasesDeDatosI.controller;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ClienteService clienteService;
    private final FacturaService facturaService;
    private final MecanicoService mecanicoService;
    private final VehiculoService vehiculoService;
    private final RepuestoService repuestoService;
    private final ProveedorService proveedorService;
    private final OrdenTrabajoService ordenTrabajoService;

    public ReporteController(ClienteService clienteService,
                             FacturaService facturaService,
                             MecanicoService mecanicoService,
                             VehiculoService vehiculoService,
                             RepuestoService repuestoService,
                             ProveedorService proveedorService,
                             OrdenTrabajoService ordenTrabajoService) {
        this.clienteService = clienteService;
        this.facturaService = facturaService;
        this.mecanicoService = mecanicoService;
        this.vehiculoService = vehiculoService;
        this.repuestoService = repuestoService;
        this.proveedorService = proveedorService;
        this.ordenTrabajoService = ordenTrabajoService;
    }

    // =========================================================
    //      REPORTES DE CLIENTES / FACTURACIÓN POR CLIENTE
    // =========================================================

    // GET /api/reportes/clientes/facturacion?inicio=2025-01-01&fin=2025-12-31
    // [idCliente, nombreCliente, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal]
    @GetMapping("/clientes/facturacion")
    public List<Object[]> resumenFacturacionClientes(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return clienteService.obtenerResumenFacturacionPorClienteEnRangoFechas(fechaInicio, fechaFin);
    }

    // =========================================================
    //      REPORTES DE FACTURAS (ESTADOS / MENSUAL)
    // =========================================================

    // GET /api/reportes/facturas/por-estado?inicio=2025-01-01&fin=2025-12-31
    // [idEstadoFactura, nombreEstado, totalFacturado, cantidadFacturas]
    @GetMapping("/facturas/por-estado")
    public List<Object[]> totalFacturadoPorEstado(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.obtenerTotalFacturadoPorEstadoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/facturas/mensual?inicio=2025-01-01&fin=2025-12-31
    // [anio, mes, totalFacturado, manoObraTotal, repuestosTotal, impuestoTotal, cantidadFacturas]
    @GetMapping("/facturas/mensual")
    public List<Object[]> facturacionMensual(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return facturaService.obtenerFacturacionMensualEnRangoFechas(fechaInicio, fechaFin);
    }

    // =========================================================
    //      REPORTES DE MECÁNICOS
    // =========================================================

    // GET /api/reportes/mecanicos/horas-costo?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    // [idMecanico, experiencia, horasTotales, costoTotal]
    @GetMapping("/mecanicos/horas-costo")
    public List<Object[]> horasYCostoPorMecanico(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return mecanicoService.obtenerHorasYCostoTotalPorMecanicoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/mecanicos/horas-por-area?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    // [idMecanico, idAreaLaboral, nombreArea, horasTotales]
    @GetMapping("/mecanicos/horas-por-area")
    public List<Object[]> horasPorMecanicoYArea(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return mecanicoService.obtenerHorasPorMecanicoYAreaEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/mecanicos/sin-ordenes
    @GetMapping("/mecanicos/sin-ordenes")
    public List<?> mecanicosSinOrdenes() {
        return mecanicoService.listarMecanicosSinOrdenes();
    }

    // =========================================================
    //      REPORTES DE VEHÍCULOS
    // =========================================================

    // GET /api/reportes/vehiculos/facturacion?inicio=2025-01-01&fin=2025-12-31
    // [idVehiculo, marca, modelo, anio, totalFacturado, cantidadFacturas]
    @GetMapping("/vehiculos/facturacion")
    public List<Object[]> facturacionPorVehiculo(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return vehiculoService.obtenerTotalFacturadoPorVehiculoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/vehiculos/ordenes
    // [idVehiculo, marca, modelo, anio, cantidadOrdenes]
    @GetMapping("/vehiculos/ordenes")
    public List<Object[]> ordenesPorVehiculo() {
        return vehiculoService.contarOrdenesPorVehiculo();
    }

    // GET /api/reportes/vehiculos/sin-ordenes
    @GetMapping("/vehiculos/sin-ordenes")
    public List<?> vehiculosSinOrdenes() {
        return vehiculoService.listarVehiculosSinOrdenes();
    }

    // =========================================================
    //      REPORTES DE REPUESTOS
    // =========================================================

    // GET /api/reportes/repuestos/stock-valor
    // [idRepuesto, nombreRepuesto, stockTotal, valorTotal]
    @GetMapping("/repuestos/stock-valor")
    public List<Object[]> stockYValorRepuestos() {
        return repuestoService.obtenerStockYValorTotalPorRepuesto();
    }

    // GET /api/reportes/repuestos/ventas?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
    // [idRepuesto, nombreRepuesto, cantidadTotal, totalVendido]
    @GetMapping("/repuestos/ventas")
    public List<Object[]> ventasRepuestosEnRango(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return repuestoService.obtenerVentasPorRepuestoEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/repuestos/compras?inicio=2025-01-01&fin=2025-12-31
    // [idRepuesto, nombreRepuesto, cantidadComprada, costoTotalCompras]
    @GetMapping("/repuestos/compras")
    public List<Object[]> comprasRepuestosEnRango(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return repuestoService.obtenerComprasPorRepuestoEnRangoFechas(fechaInicio, fechaFin);
    }

    // =========================================================
    //      REPORTES DE PROVEEDORES
    // =========================================================

    // GET /api/reportes/proveedores/suministros
    // [idProveedor, nombreProveedor, cantidadTotal, costoTotal]
    @GetMapping("/proveedores/suministros")
    public List<Object[]> resumenSuministrosPorProveedor() {
        return proveedorService.obtenerResumenSuministrosPorProveedor();
    }

    // GET /api/reportes/proveedores/costo-total?inicio=2025-01-01&fin=2025-12-31
    // [idProveedor, nombreProveedor, costoTotal]
    @GetMapping("/proveedores/costo-total")
    public List<Object[]> costoTotalProveedoresEnRango(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return proveedorService.obtenerCostoTotalPorProveedorEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/proveedores/sin-suministros
    @GetMapping("/proveedores/sin-suministros")
    public List<?> proveedoresSinSuministros() {
        return proveedorService.listarProveedoresSinSuministros();
    }

    // =========================================================
    //      REPORTES DE ÓRDENES DE TRABAJO
    // =========================================================

    // GET /api/reportes/ordenes/por-estado
    // [idEstadoOrden, nombreEstado, cantidadOrdenes]
    @GetMapping("/ordenes/por-estado")
    public List<Object[]> ordenesPorEstado() {
        return ordenTrabajoService.contarOrdenesPorEstado();
    }

    // GET /api/reportes/ordenes/por-cliente?inicio=2025-01-01&fin=2025-12-31
    // [idCliente, nombreCliente, cantidadOrdenes]
    @GetMapping("/ordenes/por-cliente")
    public List<Object[]> ordenesPorClienteEnRango(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return ordenTrabajoService.contarOrdenesPorClienteEnRangoFechas(fechaInicio, fechaFin);
    }

    // GET /api/reportes/ordenes/resumen-costos?inicio=2025-01-01&fin=2025-12-31
    // [idOrdenTrabajo, fechaIngreso, totalFacturado, manoObraTotal,
    //  repuestosTotal, impuestoTotal, cantidadFacturas]
    @GetMapping("/ordenes/resumen-costos")
    public List<Object[]> resumenCostosPorOrden(
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        return ordenTrabajoService.obtenerResumenCostosPorOrdenEnRangoFechas(fechaInicio, fechaFin);
    }
}
