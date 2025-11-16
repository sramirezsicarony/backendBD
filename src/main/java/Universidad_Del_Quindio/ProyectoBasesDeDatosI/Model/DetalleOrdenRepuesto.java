package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_orden_repuesto")
public class DetalleOrdenRepuesto {

    @EmbeddedId
    private DetalleOrdenRepuestoId id;

    @ManyToOne(optional = false)
    @MapsId("idOrdenTrabajo")
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_dor_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @ManyToOne(optional = false)
    @MapsId("idRepuesto")
    @JoinColumn(
            name = "id_repuesto",
            referencedColumnName = "id_repuesto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_dor_repuesto")
    )
    private Repuesto repuesto;

    @Column(name = "cantidad", nullable = false)
    private Short cantidad; // SMALLINT UNSIGNED

    @Column(name = "sub_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public DetalleOrdenRepuesto() {}

    public DetalleOrdenRepuesto(OrdenTrabajo ordenTrabajo, Repuesto repuesto, Short cantidad, BigDecimal subTotal) {
        this.ordenTrabajo = ordenTrabajo;
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.subTotal = subTotal;

        this.id = new DetalleOrdenRepuestoId(
                ordenTrabajo.getIdOrdenTrabajo(),
                repuesto.getIdRepuesto()
        );
    }

    // ===== Getters y Setters =====

    public DetalleOrdenRepuestoId getId() {
        return id;
    }

    public void setId(DetalleOrdenRepuestoId id) {
        this.id = id;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
