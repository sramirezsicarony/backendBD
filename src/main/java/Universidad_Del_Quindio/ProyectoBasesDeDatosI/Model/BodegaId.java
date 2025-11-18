package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class BodegaId implements Serializable {

    @Column(name = "id_almacen", nullable = false)
    private Integer idAlmacen;

    @Column(name = "id_repuesto", nullable = false)
    private Integer idRepuesto;

    public BodegaId() {}

    public BodegaId(Integer idAlmacen, Integer idRepuesto) {
        this.idAlmacen = idAlmacen;
        this.idRepuesto = idRepuesto;
    }

    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public void setIdRepuesto(Integer idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    // Necesario para PK compuesta en JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BodegaId)) return false;
        BodegaId that = (BodegaId) o;
        return Objects.equals(idAlmacen, that.idAlmacen) &&
                Objects.equals(idRepuesto, that.idRepuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlmacen, idRepuesto);
    }
}
