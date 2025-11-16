package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "estado_factura")
public class EstadoFactura {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_factura", nullable = false, updatable = false)
    private Byte idEstadoFactura; // TINYINT UNSIGNED → Short

    @Setter
    @Column(name = "estado", length = 30, nullable = false, unique = true)
    private String estado;

    // MySQL asigna automáticamente el valor al INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente el valor en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public EstadoFactura() {}

    public EstadoFactura(String estado) {
        this.estado = estado;
    }

    // ===== Getters y Setters =====

}
