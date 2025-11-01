package pos.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioMensajes implements Serializable {
    private final Usuario usuario;
    private boolean tieneMensajes;
    private final List<String> mensajes = new ArrayList<String>();

    public UsuarioMensajes(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() { return usuario; }
    public String getId()       { return usuario != null ? usuario.getId() : null; }
    public String getNombre()   { return usuario != null ? usuario.getNombre() : null; }

    public boolean mensajesPendientes() { return tieneMensajes; }
    public void setMensajesPendientes(boolean v) { this.tieneMensajes = v; }

    public void addMensaje(String m) {
        if (m != null) {
            mensajes.add(m);
            tieneMensajes = true;
        }
    }

    public String getMensaje() {
        if (mensajes.isEmpty()) return "Sin mensajes";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mensajes.size(); i++) {
            if (i > 0) sb.append('\n');
            sb.append(mensajes.get(i));
        }
        mensajes.clear();
        tieneMensajes = false;
        return sb.toString();
    }
}

