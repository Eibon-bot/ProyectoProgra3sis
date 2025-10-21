package pos.presentation.Medico;


import pos.logic.Medico;
import pos.logic.Service;

public class Controller {
    MediAdmin view;
    Model model;

    public Controller(MediAdmin view, Model model) throws Exception {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllMedico());
    }

    public void create(Medico e) throws  Exception{
        Service.instance().createMedico(e);
//        Service.instance().store();
        clear();
        model.setList(Service.instance().findAllMedico());
    }
    public void delete(Medico e) throws Exception {
        Service.instance().deleteMedico(e);
//        Service.instance().store();
        model.setCurrent(new Medico());
        model.setList(Service.instance().findAllMedico());
    }

    public void setMedico(int row) {
        Medico m = model.getList().get(row);
        model.setCurrent(m);
    }


    public void update(Medico m) throws Exception {
        Service.instance().updateMedico(m);
        model.setList(Service.instance().findAllMedico());
    }

    public void read(String nombre) throws Exception {
        try {
            model.setCurrent(Service.instance().readMedico(nombre));
        } catch (Exception ex) {
            Medico b = new Medico();
            b.setNombre(nombre);
            model.setCurrent(b);
            throw ex;
        }
    }

    public void clear() throws Exception {
        Medico m= new Medico();
        m.setId(Service.instance().generarNuevoIdMedico());
        model.setCurrent(m);
    }

    public void searchMedicoNombre(String nombre) throws Exception {
        Medico p = new Medico();
        p.setNombre(nombre);
        model.setList(Service.instance().searchMedicoNombre(p));
    }
    public void searchMedicoId(String id) throws Exception {
        Medico p = new Medico();
        p.setId(id);
        model.setList(Service.instance().searchMedicoId(p));
    }
}








