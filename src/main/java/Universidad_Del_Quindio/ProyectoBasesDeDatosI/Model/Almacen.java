package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "almacenes")
public class Almacen {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen", nullable = false, updatable = false)
    private Integer idAlmacen; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

    @Setter
    @Column(name = "direccion", length = 200, nullable = false)
    private String direccion;

    // Asignado automáticamente por MySQL en cada INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Actualizado automáticamente por MySQL en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Almacen() {}

    public Almacen(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    // ===== Getters y Setters =====

}
