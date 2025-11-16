package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "facturas")
public class Factura {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura", nullable = false, updatable = false)
    private Integer idFactura;  // INT UNSIGNED

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_factura_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_estado_factura",
            referencedColumnName = "id_estado_factura",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_factura_estado")
    )
    private EstadoFactura estadoFactura;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id_cliente",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_factura_cliente")
    )
    private Cliente cliente;

    @Setter
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Setter
    @Column(name = "sub_total_mano_de_obra", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotalManoDeObra;

    @Setter
    @Column(name = "sub_total_repuestos", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotalRepuestos;

    @Setter
    @Column(name = "impuesto", precision = 10, scale = 2, nullable = false)
    private BigDecimal impuesto;

    @Setter
    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Factura() {}

    public Factura(OrdenTrabajo ordenTrabajo,
                   EstadoFactura estadoFactura,
                   Cliente cliente,
                   LocalDate fechaCreacion,
                   BigDecimal subTotalManoDeObra,
                   BigDecimal subTotalRepuestos,
                   BigDecimal impuesto,
                   BigDecimal total) {
        this.ordenTrabajo = ordenTrabajo;
        this.estadoFactura = estadoFactura;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.subTotalManoDeObra = subTotalManoDeObra;
        this.subTotalRepuestos = subTotalRepuestos;
        this.impuesto = impuesto;
        this.total = total;
    }

    // ===== Getters y Setters =====

}
