package pos.presentation;

import pos.logic.Usuario;

public interface ThreadListener {
//    public void refresh();
    public void deliver_message(Usuario usuario, String message);
    void joined(Usuario usuario);
    void leftt(Usuario usuario);
}
