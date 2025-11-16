package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suministra")
public class Suministra {

    @EmbeddedId
    private SuministraId id;

    @ManyToOne(optional = false)
    @MapsId("idProveedor")
    @JoinColumn(
            name = "id_proveedor",
            referencedColumnName = "id_proveedor",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_suministra_proveedor")
    )
    private Proveedor proveedor;

    @ManyToOne(optional = false)
    @MapsId("idRepuesto")
    @JoinColumn(
            name = "id_repuesto",
            referencedColumnName = "id_repuesto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_suministra_repuesto")
    )
    private Repuesto repuesto;

    @Column(name = "costo_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoUnitario;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "costo_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal costoTotal;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Suministra() {}

    public Suministra(Proveedor proveedor,
                      Repuesto repuesto,
                      BigDecimal costoUnitario,
                      Integer cantidad,
                      BigDecimal costoTotal,
                      LocalDate fechaIngreso) {
        this.proveedor = proveedor;
        this.repuesto = repuesto;
        this.costoUnitario = costoUnitario;
        this.cantidad = cantidad;
        this.costoTotal = costoTotal;
        this.fechaIngreso = fechaIngreso;
        this.id = new SuministraId(
                proveedor.getIdProveedor(),
                repuesto.getIdRepuesto()
        );
    }

    // ===== Getters y Setters =====

    public SuministraId getId() {
        return id;
    }

    public void setId(SuministraId id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
