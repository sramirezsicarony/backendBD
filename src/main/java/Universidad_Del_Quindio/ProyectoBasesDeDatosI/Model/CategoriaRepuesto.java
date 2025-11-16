package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "categorias_repuesto")
public class CategoriaRepuesto {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false, updatable = false)
    private Integer idCategoria; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "categoria", length = 80, nullable = false, unique = true)
    private String categoria;

    // MySQL asigna automáticamente el valor en cada INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public CategoriaRepuesto() {}

    public CategoriaRepuesto(String categoria) {
        this.categoria = categoria;
    }

    // ===== Getters y Setters =====

}
