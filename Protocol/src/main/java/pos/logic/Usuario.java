package pos.logic;

import java.io.Serializable;
import java.util.Objects;

public abstract class Usuario implements Serializable {

    protected String id;
    protected String nombre;
    protected String clave;
    protected String rol;

    public Usuario(String id, String nombre, String clave, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rol   = rol;
    }

    public String getId()      { return id; }
    public String getNombre()  { return nombre; }
    public String getClave()   { return clave; }
    public String getRol()     { return rol;   }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setClave(String clave)   { this.clave   = clave; }
    public void setId(String id)         { this.id = id; }

    public boolean validarClave(String intento) {
        return this.clave.equals(intento);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}