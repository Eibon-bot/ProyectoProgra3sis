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

    private List<Usuario> users = new ArrayList<>();
    private String currentTarget = null;
    private final List<Mensaje> inbox = new ArrayList<>();
    private final List<Mensaje> outbox = new ArrayList<>();

    // --- getters
    public List<Usuario> getUsers(){ return users; }
    public String getCurrentTarget(){ return currentTarget; }
    public List<Mensaje> getInbox(){ return inbox; }
    public List<Mensaje> getOutbox(){ return outbox; }


    public void setUsers(List<Usuario> nueva){
        List<Usuario> old = this.users;
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
