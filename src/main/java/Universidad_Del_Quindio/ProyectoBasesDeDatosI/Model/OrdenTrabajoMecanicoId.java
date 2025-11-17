package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class OrdenTrabajoMecanicoId implements Serializable {

    @Column(name = "id_orden_trabajo", nullable = false)
    private Integer idOrdenTrabajo;

    @Column(name = "id_mecanico", length = 15, nullable = false)
    private String idMecanico;

    public OrdenTrabajoMecanicoId() {
    }

    public OrdenTrabajoMecanicoId(Integer idOrdenTrabajo, String idMecanico) {
        this.idOrdenTrabajo = idOrdenTrabajo;
        this.idMecanico = idMecanico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenTrabajoMecanicoId)) return false;
        OrdenTrabajoMecanicoId that = (OrdenTrabajoMecanicoId) o;
        return Objects.equals(idOrdenTrabajo, that.idOrdenTrabajo)
                && Objects.equals(idMecanico, that.idMecanico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdenTrabajo, idMecanico);
    }
}
