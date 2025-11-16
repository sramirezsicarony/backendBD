package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;


import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "especialidades")
public class Especialidad {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad", nullable = false, updatable = false)
    private Integer idEspecialidad; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "nombre_especialidad", length = 80, nullable = false, unique = true)
    private String nombreEspecialidad;

    @Setter
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // MySQL lo genera automáticamente al insertar
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL lo actualiza automáticamente en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Especialidad() {}

    public Especialidad(String nombreEspecialidad, String descripcion) {
        this.nombreEspecialidad = nombreEspecialidad;
        this.descripcion = descripcion;
    }

    // ===== Getters y Setters =====

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}