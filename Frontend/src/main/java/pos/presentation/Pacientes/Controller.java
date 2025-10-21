package pos.presentation.Pacientes;

import pos.logic.Paciente;
import pos.logic.Service;

import java.util.List;

public class Controller {
    PacientesAdmin view;
    Model model;

    public Controller(PacientesAdmin view, Model model) throws Exception {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllPaciente());
    }

    public void create(Paciente e) throws  Exception{
        Service.instance().createPaciente(e);
//        Service.instance().store();
        clear();
        model.setList(Service.instance().findAllPaciente());
    }

    public void delete(Paciente e) throws Exception {
        Service.instance().deletePaciente(e);
//        Service.instance().store();
        model.setCurrent(new Paciente());
        model.setList(Service.instance().findAllPaciente());
    }
    public void read(String nombre) throws Exception {
        try {
            model.setCurrent(Service.instance().readPaciente(nombre));
        } catch (Exception ex) {
            Paciente b = new Paciente();
            b.setNombre(nombre);
            model.setCurrent(b);
            throw ex;
        }
    }
    public void setPaciente(int row) {
        Paciente p = model.getList().get(row);
        model.setCurrent(p);
    }


    public void update(Paciente p) throws Exception {
        Service.instance().updatePaciente(p);
        model.setList(Service.instance().findAllPaciente());
    }


    public void selectFromList(Paciente p) {
        if (p != null) {
            model.setCurrent(p);
        }
    }

    public void clear() throws Exception {
        Paciente p = new Paciente();
        p.setId(Service.instance().generarNuevoIdPaciente());
        model.setCurrent(p);
    }

    public void searchPacienteNombre(String nombre) throws Exception {
        Paciente p = new Paciente();
        p.setNombre(nombre);
        List<Paciente> resultado = Service.instance().searchPacienteNombre(p);
        model.setList(resultado);
    }



}
