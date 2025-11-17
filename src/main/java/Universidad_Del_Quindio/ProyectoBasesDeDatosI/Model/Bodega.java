package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "bodega")
public class Bodega {

    @Setter
    @EmbeddedId
    private BodegaId id;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idAlmacen")
    @JoinColumn(
            name = "id_almacen",
            referencedColumnName = "id_almacen",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_bodega_almacen")
    )
    private Almacen almacen;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idRepuesto")
    @JoinColumn(
            name = "id_repuesto",
            referencedColumnName = "id_repuesto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_bodega_repuesto")
    )
    private Repuesto repuesto;

    @Setter
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Setter
    @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructor vacío =====
    public Bodega() {}

    // ===== Constructor completo =====
    public Bodega(Almacen almacen, Repuesto repuesto, Integer stock, BigDecimal precioVenta) {
        this.almacen = almacen;
        this.repuesto = repuesto;
        this.stock = stock;
        this.precioVenta = precioVenta;

        this.id = new BodegaId(
                almacen.getIdAlmacen(),
                repuesto.getIdRepuesto()
        );
    }

    // ===== Getters & Setters =====

}
