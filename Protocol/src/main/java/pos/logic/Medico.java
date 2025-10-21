package pos.logic;

import java.io.Serializable;
import java.util.Objects;

public class Medico extends Usuario implements Serializable {


    private String especialidad;

    public Medico(String id, String nombre, String clave, String especialidad) {
        super(id, nombre, clave, "medico");
        this.especialidad = especialidad;
    }

    public Medico() {
        this("", "", "", "");
    }

    public Medico(Usuario u) {
        super(u.getId(), u.getNombre(), u.getClave(), "medico");
        if (u instanceof Medico) {
            this.especialidad = ((Medico) u).getEspecialidad();
        } else {
            this.especialidad = "";
        }
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String esp) { this.especialidad = esp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medico medico = (Medico) o;
        return Objects.equals(getId(), medico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
