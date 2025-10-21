package pos.presentation.Farmaceutico;

import pos.presentation.AbstractModel;
import pos.logic.Farmaceutico;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    Farmaceutico current;
    List<Farmaceutico> list;


    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public Model() {
        current=new Farmaceutico("","","");
        list=new ArrayList<Farmaceutico>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Farmaceutico getCurrent() {
        return current;
    }

    public void setCurrent(Farmaceutico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Farmaceutico> getList() {
        return list;
    }

    public void setList(List<Farmaceutico> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
