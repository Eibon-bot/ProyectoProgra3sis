package pos.logic;

import java.io.Serializable;
import java.util.Objects;

public class Farmaceutico extends Usuario implements Serializable {

    public Farmaceutico(String id, String nombre, String clave) {
        super(id, nombre, clave, "farmaceutico");
    }

    public Farmaceutico() {
        this("", "", "");
    }

    public Farmaceutico(Usuario u) {
        super(u.getId(), u.getNombre(), u.getClave(), "farmaceutico");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Farmaceutico that = (Farmaceutico) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}