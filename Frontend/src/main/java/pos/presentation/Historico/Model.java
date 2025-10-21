package pos.presentation.Historico;

import pos.presentation.AbstractModel;
import pos.logic.Receta;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    Receta current;
    List<Receta> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public Model() {
        current = new Receta();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Receta> getList() {
        return list;
    }

    public void setList(List<Receta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}

