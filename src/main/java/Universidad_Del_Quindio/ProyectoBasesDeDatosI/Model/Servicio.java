package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "servicios")
public class Servicio {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio", nullable = false, updatable = false)
    private Integer idServicio; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "servicio", length = 100, nullable = false, unique = true)
    private String servicio;

    @Setter
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // MySQL asigna automáticamente al hacer INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente al hacer UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Servicio() {}

    public Servicio(String servicio, String descripcion) {
        this.servicio = servicio;
        this.descripcion = descripcion;
    }

    // ===== Getters y Setters =====

}