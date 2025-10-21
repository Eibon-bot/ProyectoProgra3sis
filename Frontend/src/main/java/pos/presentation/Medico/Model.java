package pos.presentation.Medico;

import pos.presentation.AbstractModel;
import pos.logic.Medico;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    Medico current;
    List<Medico> list;


    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public Model() {
        current=new Medico("","","","");
        list=new ArrayList<Medico>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Medico getCurrent() {
        return current;
    }

    public void setCurrent(Medico current) {
        Medico oldCurrent = this.current;
        this.current = current;
        firePropertyChange(CURRENT, oldCurrent, current);
    }


    public List<Medico> getList() {
        return list;
    }

    public void setList(List<Medico> list) {
        List<Medico> oldList = this.list;
        this.list = list;
        firePropertyChange(LIST, oldList, list);
    }

}
