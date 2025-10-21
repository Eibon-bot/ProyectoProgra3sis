package pos.presentation.Prescribir;

import pos.logic.Medicamento;
import pos.logic.Medico;
import pos.logic.Paciente;
import pos.logic.Receta;
import pos.logic.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
        Prescribir prescribirView;
        Model model;

        public Controller(Prescribir prescribirView, Model model) {
            this.prescribirView = prescribirView;
            this.model = model;
            prescribirView.setController(this);
            prescribirView.setModel(model);
        }

        //Buscar Paciente Metodos
        public void searchPacienteNombre(String nombre) throws Exception {
            Paciente p = new Paciente();
            p.setNombre(nombre);
            model.setListPaciente(Service.instance().searchPacienteNombre(p));
        }
        public void searchPacienteId(String id) throws Exception {
        Paciente p = new Paciente();
        p.setId(id);
        model.setListPaciente(Service.instance().searchPacienteId(p));
        }
        public void setPaciente(int row) {
        model.setCurrentPaciente(model.getListPaciente().get(row));
        }

        public void loadPacientes() throws Exception {
        model.setListPaciente(Service.instance().findAllPaciente());
        }
    public void searchMedicamentoNombre(String nombre) throws Exception {
        Medicamento p = new Medicamento();
        p.setNombre(nombre);
        model.setListmedicamento(Service.instance().searchMedicamentoNombre(p));
    }
    public void searchMedicamentoCod(String cod) throws Exception {
        Medicamento p = new Medicamento();
        p.setCodigo(cod);
        model.setListmedicamento(Service.instance().searchMedicamentoCod(p));
    }

    public void setMedicamento(int row) {
        model.setCurrentMedicamento(model.getListaMedicamentos().get(row));
    }

    public void loadMedicamentos() throws Exception {
        model.setListmedicamento(Service.instance().findAllMedicamento());
    }

    public Medico getCurrentMedico() {
        return model.getCurrentMedico();
    }

    public void setCurrentMedico(Medico medico) {
        model.setCurrentMedico(medico);
    }

    public void limpiarPrescripciones() {
        model.limpiarPrescripcionesTemp();
    }
    public void descartarPrescripcion(int indice) {
        model.removerPrescripcionTemp(indice);
    }



    public void guardarReceta(LocalDate fechaRetiro) {
            try{

                Receta receta = new Receta();
                receta.setPaciente(model.getCurrentPaciente());
                receta.setMedico(model.getCurrentMedico());


                receta.setFechaEmision(LocalDate.now());
                receta.setFechaRetiro(fechaRetiro);
                receta.setPrescripciones(new ArrayList<>(model.getPrescripcionesTemp()));
                receta.setEstado("Confeccionada");

                Service.instance().findAllRecetasConPrescripciones().add(receta);
                Service.instance().crearRecetaConPrescripciones(receta);
//        Service.instance().store();
                List<Receta> recetas = model.getListReceta();
                recetas.add(receta);
                model.setListReceta(recetas);
                model.setCurrentReceta(receta);

                model.limpiarPrescripcionesTemp();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }

    }











}
