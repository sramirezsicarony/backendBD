package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
public class Rol {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false, updatable = false)
    private Byte  idRol;  // TINYINT UNSIGNED → Short

    @Setter
    @Column(name = "rol", length = 50, nullable = false, unique = true)
    private String rol;

    // MySQL maneja el valor automáticamente → no se inserta ni actualiza desde Java
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Rol() {}

    public Rol(String rol) {
        this.rol = rol;
    }

    // ===== Getters y Setters =====

    public Byte  getIdRol() {
        return idRol;
    }

    public String getRol() {
        return rol;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // No se necesita setter de createdAt porque MySQL lo asigna

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}