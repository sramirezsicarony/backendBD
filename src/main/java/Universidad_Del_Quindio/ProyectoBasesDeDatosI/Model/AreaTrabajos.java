package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "area_trabajos")
public class AreaTrabajos {

    @Setter
    @EmbeddedId
    private AreaTrabajosId id;

    @Setter
    @ManyToOne(optional = false)
    @MapsId("idAreaLaboral")
    @JoinColumn(
            name = "id_area_laboral",
            referencedColumnName = "id_area_laboral",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_at_area_laboral")
    )
    private AreaLaboral areaLaboral;

    @Setter
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

}
