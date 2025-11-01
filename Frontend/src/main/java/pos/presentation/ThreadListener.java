package pos.presentation;

import pos.logic.Usuario;

public interface ThreadListener {
//    public void refresh();
    void deliver_message(Usuario usuario1,Usuario usuario2, String message);
    void joined(Usuario usuario);
    void leftt(Usuario usuario);
}
