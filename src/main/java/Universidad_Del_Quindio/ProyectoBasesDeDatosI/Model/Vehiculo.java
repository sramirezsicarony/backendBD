package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Setter
    @Id
    @Column(name = "id_vehiculo", length = 10, nullable = false, updatable = false)
    private String idVehiculo;  // Placa

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_tipo_vehiculo",
            referencedColumnName = "id_tipo_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehiculos_tipo")
    )
    private TipoVehiculo tipoVehiculo;

    @Setter
    @Column(name = "marca", length = 60, nullable = false)
    private String marca;

    @Setter
    @Column(name = "modelo", length = 60, nullable = false)
    private String modelo;

    @Setter
    @Column(name = "anio", nullable = false)
    private Short anio; // SMALLINT UNSIGNED → Integer

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id_cliente",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehiculos_cliente")
    )
    private Cliente cliente;

    // MySQL lo asigna en INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL lo actualiza en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public Vehiculo() {}

    public Vehiculo(String idVehiculo,
                    TipoVehiculo tipoVehiculo,
                    String marca,
                    String modelo,
                    Short anio,
                    Cliente cliente) {
        this.idVehiculo = idVehiculo;
        this.tipoVehiculo = tipoVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.cliente = cliente;
    }

    // ===== Getters y Setters =====

}
