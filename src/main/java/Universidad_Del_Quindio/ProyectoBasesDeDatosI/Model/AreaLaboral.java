package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "area_laboral")
public class AreaLaboral {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area_laboral", nullable = false, updatable = false)
    private Integer idAreaLaboral; // INT UNSIGNED → Integer

    @Setter
    @Column(name = "area", length = 100, nullable = false, unique = true)
    private String area;

    // Asignado automáticamente por MySQL al insertar
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Actualizado automáticamente por MySQL en cada UPDATE
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // ===== Constructores =====
    public AreaLaboral() {}

    public AreaLaboral(String area) {
        this.area = area;
    }



}
