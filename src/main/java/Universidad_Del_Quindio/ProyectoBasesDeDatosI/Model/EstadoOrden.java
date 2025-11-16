package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "estados_orden")
public class EstadoOrden {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_orden", nullable = false, updatable = false)
    private Byte idEstadoOrden; // TINYINT UNSIGNED → Short

    @Setter
    @Column(name = "estado", length = 30, nullable = false, unique = true)
    private String estado;

    // Asignado automáticamente por MySQL en cada INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Actualizado automáticamente por MySQL en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public EstadoOrden() {}

    public EstadoOrden(String estado) {
        this.estado = estado;
    }

    // ===== Getters y Setters =====

}