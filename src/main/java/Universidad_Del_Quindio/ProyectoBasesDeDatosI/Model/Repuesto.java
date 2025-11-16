package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "repuestos")
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto", nullable = false, updatable = false)
    private Integer idRepuesto;

    @Column(name = "nombre", length = 120, nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // ===== Relación con CategoriaRepuesto =====
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

    public Integer getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Integer idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaRepuesto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRepuesto categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
