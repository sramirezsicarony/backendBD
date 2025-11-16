package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "area_trabajos")
public class AreaTrabajos {

    @EmbeddedId
    private AreaTrabajosId id;

    @ManyToOne(optional = false)
    @MapsId("idAreaLaboral")
    @JoinColumn(
            name = "id_area_laboral",
            referencedColumnName = "id_area_laboral",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_at_area_laboral")
    )
    private AreaLaboral areaLaboral;

    @ManyToOne(optional = false)
    @MapsId("idMecanico")
    @JoinColumn(
            name = "id_mecanico",
            referencedColumnName = "id_mecanico",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_at_mecanico")
    )
    private Mecanico mecanico;

    // MySQL genera timestamps automáticamente
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public AreaTrabajos() {}

    public AreaTrabajos(AreaLaboral areaLaboral, Mecanico mecanico) {
        this.areaLaboral = areaLaboral;
        this.mecanico = mecanico;
        this.id = new AreaTrabajosId(
                areaLaboral.getIdAreaLaboral(),
                mecanico.getIdMecanico()
        );
    }

    // ===== Getters y Setters =====

    public AreaTrabajosId getId() {
        return id;
    }

    public void setId(AreaTrabajosId id) {
        this.id = id;
    }

    public AreaLaboral getAreaLaboral() {
        return areaLaboral;
    }

    public void setAreaLaboral(AreaLaboral areaLaboral) {
        this.areaLaboral = areaLaboral;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
