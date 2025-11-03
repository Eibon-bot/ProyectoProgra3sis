package pos.presentation.Chat;

import pos.logic.Usuario;
import pos.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.*;

public class Model extends AbstractModel {

    public static final String USUARIOS    = "usuarios";
    public static final String SELECCIONADO = "seleccionado";

    private List<Usuario> usuarios = new ArrayList<>();
    private String usuarioSeleccionadoId;


    private Map<String, List<String>> pendientes = new HashMap<>();

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(USUARIOS);
        firePropertyChange(SELECCIONADO);
    }

    public List<Usuario> getUsuarios() { return usuarios; }

    public void setUsuarios(List<Usuario> lista) {
        if (lista == null) lista = new ArrayList<>();
        this.usuarios = lista;

        for (Usuario u : lista) {
            if (u != null && !pendientes.containsKey(u.getId())) {
                pendientes.put(u.getId(), new ArrayList<>());
            }
        }
        firePropertyChange(USUARIOS);
    }

    public Usuario getUsuarioSeleccionado() {
        if (usuarioSeleccionadoId == null) return null;
        for (Usuario u : usuarios) if (u != null && usuarioSeleccionadoId.equals(u.getId())) return u;
        return null;
    }

    public void setUsuarioSeleccionado(Usuario u) {
        this.usuarioSeleccionadoId = (u == null) ? null : u.getId();
        firePropertyChange(SELECCIONADO);
    }

    // PENDIENTES
    public void addPendingMessage(String userId, String mensaje) {
        if (userId == null) return;
        pendientes.computeIfAbsent(userId, k -> new ArrayList<>()).add(mensaje);
        firePropertyChange(USUARIOS);
    }

    public String popFirstPending(String userId) {
        List<String> l = pendientes.get(userId);
        if (l == null || l.isEmpty()) return null;
        String m = l.remove(0);
        firePropertyChange(USUARIOS);
        return m;
    }

    public boolean hasPending(String userId) {
        List<String> l = pendientes.get(userId);
        return l != null && !l.isEmpty();
    }

    public void clearPending(String userId) {
        List<String> l = pendientes.get(userId);
        if (l != null) l.clear();
        firePropertyChange(USUARIOS);
    }

    public List<String> getPendingMessages(String userId) {
        List<String> l = pendientes.get(userId);
        if (l == null) return Collections.emptyList();
        return new ArrayList<>(l);
    }
}
