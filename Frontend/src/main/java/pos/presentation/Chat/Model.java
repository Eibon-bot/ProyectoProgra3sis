package pos.presentation.Chat;

import pos.logic.Mensaje;
import pos.logic.Usuario;
import pos.presentation.AbstractModel;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String USERS = "users";
    public static final String CURRENT_TARGET = "currentTarget";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";

    // AHORA: lista de IDs (String), no Usuario
    private List<String> users = new ArrayList<>();
    private String currentTarget = null;
    private final List<Mensaje> inbox = new ArrayList<>();
    private final List<Mensaje> outbox = new ArrayList<>();

    // --- getters
    public List<String> getUsers(){ return users; }          // <- retorna IDs
    public String getCurrentTarget(){ return currentTarget; }
    public List<Mensaje> getInbox(){ return inbox; }
    public List<Mensaje> getOutbox(){ return outbox; }

    // Reemplazo: setUsers(List<String>)
    public void setUsers(List<String> nueva){
        List<String> old = this.users;
        this.users = (nueva == null) ? new ArrayList<>() : nueva;
        fireOnEDT(USERS, old, this.users);
    }

    public void setCurrentTarget(String target){
        String old = this.currentTarget;
        this.currentTarget = target;
        fireOnEDT(CURRENT_TARGET, old, this.currentTarget);
    }

    public void addIncoming(Mensaje m){
        inbox.add(m);
        fireOnEDT(INBOX, null, m);
    }
    public void addOutgoing(Mensaje m){
        outbox.add(m);
        fireOnEDT(OUTBOX, null, m);
    }

    // Reemplazos: trabajar por ID
    public void addUserId(String id){
        if (id == null) return;
        if (!users.contains(id)) {
            users.add(id);
            fireOnEDT(USERS, null, users);
        }
    }
    public void removeUserId(String userId){
        if (users.remove(userId)) {
            fireOnEDT(USERS, null, users);
        }
    }
    public Mensaje popFirstFrom(String fromId){
        for (int i=0;i<inbox.size();i++){
            Mensaje m = inbox.get(i);
            if (m.getFrom().equals(fromId)) { inbox.remove(i); return m; }
        }
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        super.addPropertyChangeListener(l);
        fireOnEDT(USERS, null, users);
        fireOnEDT(CURRENT_TARGET, null, currentTarget);
    }

    private void fireOnEDT(String prop, Object oldV, Object newV){
        if (SwingUtilities.isEventDispatchThread()) {
            firePropertyChange(prop, oldV, newV);
        } else {
            SwingUtilities.invokeLater(() -> firePropertyChange(prop, oldV, newV));
        }
    }
}
