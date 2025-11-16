
package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "clientes")
public class Cliente {

    @Setter
    @Id
    @Column(name = "id_cliente", length = 15, nullable = false, updatable = false)
    private String idCliente; // VARCHAR(15) → String (sin AUTO_INCREMENT)

    @Setter
    @Column(name = "nombre", length = 120, nullable = false)
    private String nombre;

    @Setter
    @Column(name = "telefono", length = 15, nullable = false, unique = true)
    private String telefono;

    @Setter
    @Column(name = "correo", length = 100, nullable = false, unique = true)
    private String correo;

    // Asignado automáticamente por MySQL al INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Actualizado automáticamente por MySQL al UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Cliente() {}

    public Cliente(String idCliente, String nombre, String telefono, String correo) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

}
