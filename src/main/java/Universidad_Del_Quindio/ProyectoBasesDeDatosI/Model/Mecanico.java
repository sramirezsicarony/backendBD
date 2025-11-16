package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mecanicos")
public class Mecanico {

    @Id
    @Column(name = "id_mecanico", length = 15, nullable = false, updatable = false)
    private String idMecanico;

    @Column(name = "experiencia", nullable = false)
    private Byte experiencia; // TINYINT UNSIGNED → Short

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_especialidad",
            referencedColumnName = "id_especialidad",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mecanicos_especialidad")
    )
    private Especialidad especialidad;

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

    public String getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(String idMecanico) {
        this.idMecanico = idMecanico;
    }

    public Byte getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Byte experiencia) {
        this.experiencia = experiencia;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public BigDecimal getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(BigDecimal costoHora) {
        this.costoHora = costoHora;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
