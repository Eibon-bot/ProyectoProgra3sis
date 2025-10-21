package pos.presentation.Farmaceutico;

import pos.logic.Farmaceutico;
import pos.logic.Service;

public class Controller {
    FarmaAdmin view;
    Model model;

    public Controller(FarmaAdmin view, Model model) throws Exception {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllFarmaceutico());
    }

    public void create(Farmaceutico e) throws  Exception{
        Service.instance().createFarmaceutico(e);
//        Service.instance().store();
        clear();
        model.setList(Service.instance().findAllFarmaceutico());
    }

    public void setFarmaceutico(int row) {
        Farmaceutico f = model.getList().get(row);
        model.setCurrent(f);
    }
    public void delete(Farmaceutico e) throws Exception {
        Service.instance().deleteFarmaceutico(e);
//        Service.instance().store();
        model.setCurrent(new Farmaceutico());
        model.setList(Service.instance().findAllFarmaceutico());
    }

    public void update(Farmaceutico f) throws Exception {
        Service.instance().updateFarmaceutico(f);
        model.setList(Service.instance().findAllFarmaceutico());
    }

    public void read(String nombre) throws Exception {
        try {
            model.setCurrent(Service.instance().readFarmaceutico(nombre));
        } catch (Exception ex) {
            Farmaceutico b = new Farmaceutico();
            b.setNombre(nombre);
            model.setCurrent(b);
            throw ex;
        }
    }

    public void clear() throws Exception {
        Farmaceutico f = new Farmaceutico();
        f.setId(Service.instance().generarNuevoIdFarma());
        model.setCurrent(f);
    }

    public void searchFarmaceuticoNombre(String nombre) throws Exception {
        Farmaceutico p = new Farmaceutico();
        p.setNombre(nombre);
        model.setList(Service.instance().searchFarmaceuticoNombre(p));
    }
    public void searchFarmaceuticoId(String id) throws Exception {
        Farmaceutico p = new Farmaceutico();
        p.setId(id);
        model.setList(Service.instance().searchFarmaceuticoId(p));
    }
}








