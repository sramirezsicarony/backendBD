package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class AreaTrabajosId implements Serializable {

    @Column(name = "id_area_laboral", nullable = false)
    private Integer idAreaLaboral;

    @Column(name = "id_mecanico", length = 15, nullable = false)
    private String idMecanico;

    public AreaTrabajosId() {}

    public AreaTrabajosId(Integer idAreaLaboral, String idMecanico) {
        this.idAreaLaboral = idAreaLaboral;
        this.idMecanico = idMecanico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaTrabajosId)) return false;
        AreaTrabajosId that = (AreaTrabajosId) o;
        return Objects.equals(idAreaLaboral, that.idAreaLaboral) &&
                Objects.equals(idMecanico, that.idMecanico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAreaLaboral, idMecanico);
    }
}
