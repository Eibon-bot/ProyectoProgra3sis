package pos.presentation.Chat;

import pos.logic.Mensaje;
import pos.logic.Service;
import pos.logic.Usuario;

import javax.swing.*;
import java.util.List;

public class Controller {
    private Model model;
    private ChatView view;

    public Controller(ChatView view, Model model)throws Exception {
        this.view = view;
        this.model = model;

        view.setModel(model);
        view.setController(this);

        Service.instance().addListener(new Service.ServiceListener() {
            @Override public void onUserList(List<String> ids) {      // <- String, no Usuario
                model.setUsers(ids);                                   // <- ver notas del Model abajo
            }
            @Override public void onUserJoined(String userId) {        // <- String
                model.addUserId(userId);                               // <- ver notas del Model abajo
            }
            @Override public void onUserLeft(String userId) {          // <- String
                model.removeUserId(userId);                            // <- ver notas del Model abajo
            }
            @Override public void onMessage(String fromId, String text) {
                Mensaje m = new Mensaje(fromId, model.getCurrentTarget(), text);
                model.addIncoming(m);
            }
        });



    }

    public void enviar(String texto) {
        String to = model.getCurrentTarget();
        if (to == null || to.isEmpty()) return;
        try {
            Service.instance().sendMessage(to, texto);
            Mensaje m = new Mensaje(/*from*/"yo", to, texto); // ajusta "yo" si tienes tu id
            model.addOutgoing(m);
            model.addIncoming(m); // si quieres eco local
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getPanelchat(),"No se pudo enviar: "+e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Mensaje popMensajePendiente(String fromId){
        return model.popFirstFrom(fromId);
    }


    public void setDestino(String userId){
        model.setCurrentTarget(userId);
    }

    public void setMensajesFlag(String userId, boolean value){
//        Service.setFlag(userId, value);
    }
}
