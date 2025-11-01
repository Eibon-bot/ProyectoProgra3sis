package pos.presentation.Chat;

import pos.logic.Mensaje;
import pos.logic.Service;
import pos.logic.Usuario;
import pos.logic.UsuarioMensajes;
import pos.presentation.SocketListener;
import pos.presentation.ThreadListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller implements ThreadListener {
    private Model model;
    private ChatView view;
    SocketListener sl;

    public Controller(ChatView view, Model model)throws Exception {
        this.view = view;
        this.model = model;

        view.setModel(model);
        view.setController(this);

        try{
            sl= new SocketListener(this,((Service) Service.instance()).getSid());
            sl.start();
        }catch(Exception e){}

    }

    public void enviar(String mensaje) throws Exception{

        Service.instance().sendMessage(model.getUsuarioSeleccionado().getUsuario(),mensaje);
    }
//    public Mensaje popMensajePendiente(String fromId){
//        return model.popFirstFrom(fromId);
//    }


//    public void setDestino(String userId){
//        model.setCurrentTarget(userId);
//    }

    public void setMensajesFlag(String userId, boolean value){
//        Service.setFlag(userId, value);
    }

    public void seleccionarPorId(String userId) {
        if (userId == null) return;
        List<UsuarioMensajes> lista = model.getUsuarios();
        if (lista == null) return;

        for (int i = 0; i < lista.size(); i++) {
            UsuarioMensajes um = lista.get(i);
            if (um == null || um.getUsuario() == null) continue;
            String rid = um.getUsuario().getId();
            if ((userId == null && rid == null) || (userId != null && userId.equals(rid))) {
                model.setUsuarioSeleccionado(um);
                return;
            }
        }
    }




    @Override
    public void joined(Usuario usuario) {
        UsuarioMensajes nuevo = new UsuarioMensajes(usuario);
        List<UsuarioMensajes> n = new ArrayList<>(model.getUsuarios());
        n.add(nuevo);
        model.setUsuarios(n);
        System.out.println(n.size());

    }

    @Override
    public void leftt(Usuario usuario) {
        List<UsuarioMensajes> n = new ArrayList<>(model.getUsuarios());
        n.removeIf(u -> u.getUsuario().getId().equals(usuario.getId()));
        model.setUsuarios(n);

    }
    @Override
    public void deliver_message(Usuario usuario, String message) {
        List<UsuarioMensajes> n = new ArrayList<>(model.getUsuarios());
        for (UsuarioMensajes u : n){
            if(Objects.equals(u.getUsuario().getId(), usuario.getId())){
                u.addMensaje(message);
            }
        }

        model.setUsuarios(n);
    }
}
