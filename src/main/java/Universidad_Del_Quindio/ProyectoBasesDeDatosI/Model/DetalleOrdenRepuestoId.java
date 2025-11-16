package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DetalleOrdenRepuestoId implements Serializable {

    @Column(name = "id_orden_trabajo", nullable = false)
    private Integer idOrdenTrabajo;

    @Column(name = "id_repuesto", nullable = false)
    private Integer idRepuesto;

    public DetalleOrdenRepuestoId() {}

    public DetalleOrdenRepuestoId(Integer idOrdenTrabajo, Integer idRepuesto) {
        this.idOrdenTrabajo = idOrdenTrabajo;
        this.idRepuesto = idRepuesto;
    }

    public Integer getIdOrdenTrabajo() {
        return idOrdenTrabajo;
    }

    public void setIdOrdenTrabajo(Integer idOrdenTrabajo) {
        this.idOrdenTrabajo = idOrdenTrabajo;
    }

    public Integer getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Integer idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleOrdenRepuestoId)) return false;
        DetalleOrdenRepuestoId that = (DetalleOrdenRepuestoId) o;
        return Objects.equals(idOrdenTrabajo, that.idOrdenTrabajo)
                && Objects.equals(idRepuesto, that.idRepuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdenTrabajo, idRepuesto);
    }
}
