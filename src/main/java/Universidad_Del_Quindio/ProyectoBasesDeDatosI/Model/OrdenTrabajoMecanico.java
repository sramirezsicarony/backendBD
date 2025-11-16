package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_trabajo_mecanico")
public class OrdenTrabajoMecanico {

    @EmbeddedId
    private OrdenTrabajoMecanicoId id;

    @ManyToOne(optional = false)
    @MapsId("idOrdenTrabajo")
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @ManyToOne(optional = false)
    @MapsId("idMecanico")
    @JoinColumn(
            name = "id_mecanico",
            referencedColumnName = "id_mecanico",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_mecanico")
    )
    private Mecanico mecanico;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_rol",
            referencedColumnName = "id_rol",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_otm_rol")
    )
    private Rol rol;

    @Column(name = "horas", precision = 5, scale = 2, nullable = false)
    private BigDecimal horas;

    @Column(name = "costo_hora", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoHora;

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

    public OrdenTrabajoMecanicoId getId() {
        return id;
    }

    public void setId(OrdenTrabajoMecanicoId id) {
        this.id = id;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }

    public BigDecimal getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(BigDecimal costoHora) {
        this.costoHora = costoHora;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
