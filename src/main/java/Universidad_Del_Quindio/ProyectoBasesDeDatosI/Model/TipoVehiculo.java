package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "tipo_vehiculo")
public class TipoVehiculo {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_vehiculo", nullable = false, updatable = false)
    private Integer idTipoVehiculo; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "tipo", length = 50, nullable = false, unique = true)
    private String tipo;

    // MySQL asigna el valor al insertar
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL actualiza automáticamente en UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public TipoVehiculo() {}

    public TipoVehiculo(String tipo) {
        this.tipo = tipo;
    }

    // ===== Getters y Setters =====

}