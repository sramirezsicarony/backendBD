package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orden_trabajo_mecanico")
public class OrdenTrabajoMecanico {

    @Setter
    @EmbeddedId
    private OrdenTrabajoMecanicoId id;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idOrdenTrabajo")
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idMecanico")
    @JoinColumn(
            name = "id_mecanico",
            referencedColumnName = "id_mecanico",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_mecanico")
    )
    private Mecanico mecanico;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_rol",
            referencedColumnName = "id_rol",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_rol")
    )
    private Rol rol;

    @Setter
    @Column(name = "horas", precision = 5, scale = 2, nullable = false)
    private BigDecimal horas;

    @Setter
    @Column(name = "costo_hora", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoHora;

    @Setter
    @Column(name = "costo_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal costoTotal;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public OrdenTrabajoMecanico() {
    }

    public OrdenTrabajoMecanico(OrdenTrabajo ordenTrabajo,
                                Mecanico mecanico,
                                Rol rol,
                                BigDecimal horas,
                                BigDecimal costoHora,
                                BigDecimal costoTotal) {
        this.ordenTrabajo = ordenTrabajo;
        this.mecanico = mecanico;
        this.rol = rol;
        this.horas = horas;
        this.costoHora = costoHora;
        this.costoTotal = costoTotal;
        this.id = new OrdenTrabajoMecanicoId(
                ordenTrabajo.getIdOrdenTrabajo(),
                mecanico.getIdMecanico()
        );
    }

    // ===== Getters y Setters =====

}
