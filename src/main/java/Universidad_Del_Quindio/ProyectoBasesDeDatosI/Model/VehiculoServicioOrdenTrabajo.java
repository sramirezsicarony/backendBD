package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "vehiculo_servicio_orden_trabajo")
public class VehiculoServicioOrdenTrabajo {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo_servicio_orden_trabajo", nullable = false, updatable = false)
    private Integer idVehiculoServicioOrdenTrabajo;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_vehiculo",
            referencedColumnName = "id_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vso_vehiculo")
    )
    private Vehiculo vehiculo;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_servicio",
            referencedColumnName = "id_servicio",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vso_servicio")
    )
    private Servicio servicio;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_orden_trabajo",
            referencedColumnName = "id_orden_trabajo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vso_orden")
    )
    private OrdenTrabajo ordenTrabajo;

    @Setter
    @Column(name = "fecha_de_ejecucion", nullable = false)
    private LocalDate fechaDeEjecucion;

    // Generado automáticamente por MySQL
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Actualizado automáticamente por MySQL
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public VehiculoServicioOrdenTrabajo() {}

    public VehiculoServicioOrdenTrabajo(
            Vehiculo vehiculo,
            Servicio servicio,
            OrdenTrabajo ordenTrabajo,
            LocalDate fechaDeEjecucion
    ) {
        this.vehiculo = vehiculo;
        this.servicio = servicio;
        this.ordenTrabajo = ordenTrabajo;
        this.fechaDeEjecucion = fechaDeEjecucion;
    }

    // ===== Getters y Setters =====

}
