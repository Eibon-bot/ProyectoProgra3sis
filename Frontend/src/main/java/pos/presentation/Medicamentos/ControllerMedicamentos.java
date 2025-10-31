package pos.presentation.Medicamentos;

import pos.logic.Medicamento;
import pos.logic.Service;

public class ControllerMedicamentos {
    MedicaAdmin view;
    ModelMedicamentos model;

    public ControllerMedicamentos(MedicaAdmin view, ModelMedicamentos model) throws Exception {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllMedicamento());
    }

    public void create(Medicamento e) throws  Exception{
        Service.instance().createMedicamento(e);
//        Service.instance().store();
        clear();
        model.setList(Service.instance().findAllMedicamento());
    }
    public void delete(Medicamento e) throws Exception {
        Service.instance().deleteMedicamento(e);
//        Service.instance().store();
        model.setCurrent(new Medicamento());
        model.setList(Service.instance().findAllMedicamento());
    }

    public void setMedicamento(int row) {
        Medicamento p = model.getList().get(row);
        model.setCurrent(p);
    }

    public void update(Medicamento p) throws Exception {
        Medicamento original = model.getCurrent();
        if (original == null || original.getCodigo() == null) {
            throw new Exception("Codigo nulo");
        }
        p.setCodigo(original.getCodigo());
        Service.instance().updateMedicamento(p);
        model.setList(Service.instance().findAllMedicamento());
    }

    public void read(String nombre) throws Exception {
        try {
            model.setCurrent(Service.instance().readMedicamento(nombre));
        } catch (Exception ex) {
            Medicamento b = new Medicamento();
            b.setNombre(nombre);
            model.setCurrent(b);
            throw ex;
        }
    }

    public void clear() {
        Medicamento m= new Medicamento();
//        m.setCodigo(Service.instance().generarNuevoCodMedicamento(m.getNombre()));
        model.setCurrent(m);
    }
    public void searchMedicamentoNombre(String nombre) throws Exception {
        Medicamento p = new Medicamento();
        p.setNombre(nombre);
        model.setList(Service.instance().searchMedicamentoNombre(p));
    }

    public void searchMedicamentoCodigo(String id) throws Exception {
        Medicamento p = new Medicamento();
        p.setCodigo(id);
        model.setList(Service.instance().searchMedicamentoCod(p));
    }
}
