package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orden_trabajo")
public class OrdenTrabajo {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_trabajo", nullable = false, updatable = false)
    private Integer idOrdenTrabajo; // INT UNSIGNED

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "id_vehiculo",
            referencedColumnName = "id_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ot_vehiculo")
    )
    private Vehiculo vehiculo;

    @Setter
    @Column(name = "diagnostico_inicial", nullable = false, columnDefinition = "TEXT")
    private String diagnosticoInicial;

    @Setter
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Setter
    @ManyToOne(optional = true)
    @JoinColumn(
            name = "id_estado_orden",
            referencedColumnName = "id_estado_orden",
            foreignKey = @ForeignKey(name = "fk_ot_estado")
    )
    private EstadoOrden estadoOrden;  // puede ser null (ON DELETE SET NULL)

    @Setter
    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    // MySQL lo genera al INSERT
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // MySQL lo actualiza al UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public OrdenTrabajo() {}

    public OrdenTrabajo(Vehiculo vehiculo,
                        String diagnosticoInicial,
                        LocalDate fechaIngreso,
                        EstadoOrden estadoOrden,
                        LocalDate fechaSalida) {
        this.vehiculo = vehiculo;
        this.diagnosticoInicial = diagnosticoInicial;
        this.fechaIngreso = fechaIngreso;
        this.estadoOrden = estadoOrden;
        this.fechaSalida = fechaSalida;
    }

    // ===== Getters y Setters =====

}
