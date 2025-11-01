package pos.presentation.Chat;

import pos.logic.Mensaje;
import pos.logic.Usuario;
import pos.logic.UsuarioMensajes;
import pos.presentation.AbstractModel;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    public static final String USUARIOS    = "usuarios";
    public static final String SELECCIONADO = "seleccionado";

    private List<UsuarioMensajes> usuarios = new ArrayList<UsuarioMensajes>();
    private UsuarioMensajes usuarioSeleccionado;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(USUARIOS);
        firePropertyChange(SELECCIONADO);
    }

    public List<UsuarioMensajes> getUsuarios() { return usuarios; }

    public void setUsuarios(List<UsuarioMensajes> lista) {
        if (lista == null) lista = new ArrayList<UsuarioMensajes>();
        this.usuarios = lista;
        firePropertyChange(USUARIOS);
    }

    public UsuarioMensajes getUsuarioSeleccionado() { return usuarioSeleccionado; }

    public void setUsuarioSeleccionado(UsuarioMensajes u) {
        this.usuarioSeleccionado = u;
        firePropertyChange(SELECCIONADO);
    }
}
