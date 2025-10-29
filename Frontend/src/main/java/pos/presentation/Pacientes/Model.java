package pos.presentation.Pacientes;

import pos.presentation.AbstractModel;
import pos.logic.Paciente;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String CURRENT = "current";
    public static final String LIST = "list";

    private Paciente current;
    private List<Paciente> list;

    public Model() {
        current = new Paciente();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Paciente getCurrent() { return current; }
    public void setCurrent(Paciente current) {
        Paciente old = this.current;
        this.current = (current == null) ? new Paciente() : current;
        firePropertyChange(CURRENT, old, this.current);
    }

    public List<Paciente> getList() { return list; }
    public void setList(List<Paciente> list) {
        List<Paciente> old = this.list;
        this.list = (list == null) ? new ArrayList<>() : list;
        firePropertyChange(LIST, old, this.list);
    }

}
