package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "repuestos")
public class Repuesto {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto", nullable = false, updatable = false)
    private Integer idRepuesto;

    @Setter
    @Column(name = "nombre", length = 120, nullable = false, unique = true)
    private String nombre;

    @Setter
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // ===== Relación con CategoriaRepuesto =====
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_categoria_repuesto",
            referencedColumnName = "id_categoria",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_repuestos_categoria")
    )
    private CategoriaRepuesto categoria;

    // Autogenerado por MySQL
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Autogenerado por MySQL en UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Repuesto() {}

    public Repuesto(String nombre, String descripcion, CategoriaRepuesto categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    // ===== Getters y Setters =====

}
