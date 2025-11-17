package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "suministra")
public class Suministra {

    @Setter
    @EmbeddedId
    private SuministraId id;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idProveedor")
    @JoinColumn(
            name = "id_proveedor",
            referencedColumnName = "id_proveedor",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_suministra_proveedor")
    )
    private Proveedor proveedor;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idRepuesto")
    @JoinColumn(
            name = "id_repuesto",
            referencedColumnName = "id_repuesto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_suministra_repuesto")
    )
    private Repuesto repuesto;

    @Setter
    @Column(name = "costo_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoUnitario;

    @Setter
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Setter
    @Column(name = "costo_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal costoTotal;

    @Setter
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

}
