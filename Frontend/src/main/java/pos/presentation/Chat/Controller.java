package pos.presentation.Chat;

import pos.logic.Service;
import pos.logic.Usuario;
import pos.presentation.SocketListener;
import pos.presentation.ThreadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller implements ThreadListener {
    private Model model;
    private ChatView view;
    SocketListener sl;

    public Controller(ChatView view, Model model) throws Exception {
        this.view = view;
        this.model = model;

        view.setModel(model);
        view.setController(this);

        try {
            sl = new SocketListener(this, ((Service) Service.instance()).getSid());
            sl.start();
        } catch (Exception e) { }
    }

    public void enviar(String mensaje) throws Exception {
        Usuario destino = model.getUsuarioSeleccionado();
        if (destino == null) throw new IllegalStateException("No hay destinatario seleccionado");
        Service.instance().sendMessage(destino, mensaje);
    }

    public void setMensajesFlag(String userId, boolean value) {
        if (userId == null) return;
        if (value) {
            model.addPendingMessage(userId, "[pendiente]");
        } else {
            model.clearPending(userId);
        }
    }

    public void confirmReceived(String fromId) {
        if (fromId == null) return;
        // quitar solo el primer mensaje pendiente
        String m = model.popFirstPending(fromId);
        // si ya no hay pendientes, nada más (vista reflejará cambios vía listeners)
    }

    public void seleccionarPorId(String userId) {
        if (userId == null) return;
        List<Usuario> lista = model.getUsuarios();
        if (lista == null) return;
        for (Usuario u : lista) {
            if (u == null) continue;
            if ((userId == null && u.getId() == null) || (userId != null && userId.equals(u.getId()))) {
                model.setUsuarioSeleccionado(u);
                return;
            }
        }
    }

    @Override
    public void joined(Usuario usuario) {
        List<Usuario> n = new ArrayList<>(model.getUsuarios());
        n.add(usuario);
        model.setUsuarios(n);
    }

    @Override
    public void leftt(Usuario usuario) {
        List<Usuario> n = new ArrayList<>(model.getUsuarios());
        n.removeIf(u -> u == null || u.getId() == null ? false : u.getId().equals(usuario.getId()));
        model.setUsuarios(n);
    }

    @Override
    public void deliver_message(Usuario usuarioEmisor, String message) {
        if (usuarioEmisor == null || message == null) return;
        model.addPendingMessage(usuarioEmisor.getId(), message);
    }
}
