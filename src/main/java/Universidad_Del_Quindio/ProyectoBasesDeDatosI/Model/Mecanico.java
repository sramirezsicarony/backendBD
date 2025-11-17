package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "mecanicos")
public class Mecanico {

    @Setter
    @Id
    @Column(name = "id_mecanico", length = 15, nullable = false, updatable = false)
    private String idMecanico;

    @Setter
    @Column(name = "experiencia", nullable = false)
    private Byte experiencia; // TINYINT UNSIGNED → Short

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_especialidad",
            referencedColumnName = "id_especialidad",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mecanicos_especialidad")
    )
    private Especialidad especialidad;

    @Setter
    @Column(name = "costo_hora", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoHora;

    // MySQL lo asigna automáticamente al INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL lo actualiza automáticamente en UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Mecanico() {}

    public Mecanico(String idMecanico, Byte experiencia, Especialidad especialidad, BigDecimal costoHora) {
        this.idMecanico = idMecanico;
        this.experiencia = experiencia;
        this.especialidad = especialidad;
        this.costoHora = costoHora;
    }

    // ===== Getters y Setters =====

}
