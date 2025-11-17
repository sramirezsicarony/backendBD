package Universidad_Del_Quindio.ProyectoBasesDeDatosI.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class SuministraId implements Serializable {

    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "id_repuesto", nullable = false)
    private Integer idRepuesto;

    public SuministraId() {}

    public SuministraId(Integer idProveedor, Integer idRepuesto) {
        this.idProveedor = idProveedor;
        this.idRepuesto = idRepuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuministraId)) return false;
        SuministraId that = (SuministraId) o;
        return Objects.equals(idProveedor, that.idProveedor)
                && Objects.equals(idRepuesto, that.idRepuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProveedor, idRepuesto);
    }
}
