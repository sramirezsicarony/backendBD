package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false, updatable = false)
    private Integer idProveedor; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "nombre", length = 120, nullable = false, unique = true)
    private String nombre;

    @Setter
    @Column(name = "telefono", length = 15, nullable = false, unique = true)
    private String telefono;

    @Setter
    @Column(name = "direccion", length = 200, nullable = false)
    private String direccion;

    // MySQL asigna automáticamente al hacer INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Proveedor() {}

    public Proveedor(String nombre, String telefono, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // ===== Getters y Setters =====

}
