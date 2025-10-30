package pos.presentation.Chat;

import pos.logic.Mensaje;
import pos.logic.Service;

public class Controller {
    private Model model;
    private ChatView view;

    public Controller(ChatView view, Model model)throws Exception {
        this.view = view;
        this.model = model;

        view.setModel(model);
        view.setController(this);

    }

    public void enviar(String texto) {
        String to = model.getCurrentTarget();
        if (to == null || to.isEmpty()) return;
//        Mensaje m = new Mensaje(Service.getSelfId(), to, texto);
//        Service.send(m);
//        model.addOutgoing(m);
//        model.addIncoming(m);
    }

    public void setDestino(String userId){
        model.setCurrentTarget(userId);
    }

    public void setMensajesFlag(String userId, boolean value){
//        Service.setFlag(userId, value);
    }
}
