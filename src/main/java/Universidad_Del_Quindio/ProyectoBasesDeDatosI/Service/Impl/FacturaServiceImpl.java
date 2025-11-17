package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.Impl;

import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model.Factura;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Repository.FacturaRepository;
import Universidad_Del_Quindio.ProyectoBasesDeDatosI.Service.FacturaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;

    // Inyección por constructor
    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    // ====================== CRUD BÁSICO ======================

    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Factura obtenerPorId(Integer idFactura) {
        return facturaRepository.findById(idFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró una factura con id: " + idFactura
                ));
    }

    @Override
    public Factura crear(Factura factura) {
        // id_factura es autogenerado por la BD
        return facturaRepository.save(factura);
    }

    @Override
    public Factura actualizar(Integer idFactura, Factura facturaActualizada) {
        Factura existente = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede actualizar. No existe una factura con id: " + idFactura
                ));

        // Actualizamos campos modificables (no tocamos id, createdAt, updatedAt)
        existente.setOrdenTrabajo(facturaActualizada.getOrdenTrabajo());
        existente.setEstadoFactura(facturaActualizada.getEstadoFactura());
        existente.setCliente(facturaActualizada.getCliente());
        existente.setFechaCreacion(facturaActualizada.getFechaCreacion());
        existente.setSubTotalManoDeObra(facturaActualizada.getSubTotalManoDeObra());
        existente.setSubTotalRepuestos(facturaActualizada.getSubTotalRepuestos());
        existente.setImpuesto(facturaActualizada.getImpuesto());
        existente.setTotal(facturaActualizada.getTotal());

        return facturaRepository.save(existente);
    }

    @Override
    public void eliminar(Integer idFactura) {
        Factura existente = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se puede eliminar. No existe una factura con id: " + idFactura
                ));

        facturaRepository.delete(existente);
    }

    // ========== MÉTODOS QUE USAN LAS CONSULTAS DEL REPOSITORY ==========

    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorIdCliente(String idCliente) {
        return facturaRepository.findByCliente_IdCliente(idCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorIdEstadoFactura(Byte idEstadoFactura) {
        return facturaRepository.findByEstadoFactura_IdEstadoFactura(idEstadoFactura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> buscarPorFechaCreacionEntre(LocalDate inicio, LocalDate fin) {
        return facturaRepository.findByFechaCreacionBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> buscarPorTotalEntre(BigDecimal totalMin, BigDecimal totalMax) {
        return facturaRepository.findByTotalBetween(totalMin, totalMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerResumenFacturacionPorClienteEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return facturaRepository.resumenFacturacionPorClienteEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerTotalFacturadoPorEstadoEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return facturaRepository.totalFacturadoPorEstadoEnRangoFechas(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerFacturacionMensualEnRangoFechas(LocalDate inicio, LocalDate fin) {
        return facturaRepository.facturacionMensualEnRangoFechas(inicio, fin);
    }
}
