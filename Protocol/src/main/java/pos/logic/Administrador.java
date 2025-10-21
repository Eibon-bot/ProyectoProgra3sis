package pos.logic;

import java.io.Serializable;
import java.util.Objects;

public class Administrador extends Usuario implements Serializable {

    public Administrador(String id, String nombre, String clave) {
        super(id, nombre, clave, "administrador");
    }

    public Administrador(Usuario u) {
        super(u.getId(), u.getNombre(), u.getClave(), "administrador");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrador that = (Administrador) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

