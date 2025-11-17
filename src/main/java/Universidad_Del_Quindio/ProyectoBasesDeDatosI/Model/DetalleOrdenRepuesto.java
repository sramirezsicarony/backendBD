package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "detalle_orden_repuesto")
public class DetalleOrdenRepuesto {

    @Setter
    @EmbeddedId
    private DetalleOrdenRepuestoId id;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idOrdenTrabajo")
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_dor_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idRepuesto")
    @JoinColumn(
            name = "id_repuesto",
            referencedColumnName = "id_repuesto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_dor_repuesto")
    )
    private Repuesto repuesto;

    @Setter
    @Column(name = "cantidad", nullable = false)
    private Short cantidad; // SMALLINT UNSIGNED

    @Setter
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

}
