package pos.presentation.Prescribir;

import pos.presentation.AbstractModel;
import pos.logic.*;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    Paciente paciente;
    Receta receta;
    Medicamento medicamento;
    Medico medico;
    private List<Medicamento> listaMedicamentos;
    private List<Receta> listReceta;
    private List<Paciente> listPaciente;
    private List<Prescripcion> prescripcionesTemp = new ArrayList<>();


    public static final String PACIENTE = "paciente";
    public static final String RECETA = "receta";
    public static final String MEDICAMENTO = "medicamento";
    public static final String MEDICO = "medico";
    public static final String LISTMEDICAMENTO = "listMedicamento";
    public static final String LISTPACIENTE = "listPaciente";
    public static final String LISTRECETA = "listReceta";
    public static final String PRESCRIPCION_TEMP = "prescripcionTemp";

    public Model() {
        paciente = null;
        receta = null;
        medicamento = null;
        medico = null;
        listaMedicamentos = new ArrayList<>();
        listReceta = new ArrayList<>();
        listPaciente = new ArrayList<>();
        prescripcionesTemp=new ArrayList<>();

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(PACIENTE);
        firePropertyChange(RECETA);
        firePropertyChange(MEDICAMENTO);
        firePropertyChange(MEDICO);
        firePropertyChange(LISTMEDICAMENTO);
        firePropertyChange(PRESCRIPCION_TEMP);
    }

    public Paciente getCurrentPaciente() {
        return paciente;
    }

    public Medicamento getCurrentMedicamento() {
        return medicamento;
    }

    public Receta getCurrentReceta() {
        return receta;
    }

    public Medico getCurrentMedico() {
        return medico;
    }

    public void setCurrentPaciente(Paciente paciente) {
        this.paciente = paciente;
        firePropertyChange(PACIENTE);
    }

    public void setCurrentMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
        firePropertyChange(MEDICAMENTO);
    }

    public void setCurrentReceta(Receta receta) {
        this.receta = receta;
        firePropertyChange(RECETA);
    }

    public void setCurrentMedico(Medico medico) {
        this.medico = medico;
        firePropertyChange(MEDICO);
    }

    //Listas
    public List<Paciente> getListPaciente() {
        return listPaciente;
    }

    public void setListPaciente(List<Paciente> listPaciente) {
        this.listPaciente = listPaciente;
        firePropertyChange(LISTPACIENTE);
    }

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListmedicamento(List<Medicamento> listMedicamento) {
        this.listaMedicamentos = listMedicamento;
        firePropertyChange(LISTMEDICAMENTO);
    }


    public List<Receta> getListReceta() {
        return listReceta;
    }

    public void setListReceta(List<Receta> listReceta) {
        this.listReceta = listReceta;
        firePropertyChange(LISTRECETA);
    }

    public void notifyRecetaChanged() {
        firePropertyChange(RECETA);
    }

    public List<Prescripcion> getPrescripcionesTemp() {
        return prescripcionesTemp;
    }

    public void agregarPrescripcionTemp(Prescripcion p) {
        prescripcionesTemp.add(p);
        firePropertyChange("PRESCRIPCION_TEMP", null, p);
    }

    public void limpiarPrescripcionesTemp() {
        prescripcionesTemp.clear();
        firePropertyChange("PRESCRIPCION_TEMP", null, null);
    }
    public void removerPrescripcionTemp(int indice) {
        if (indice >= 0 && indice < prescripcionesTemp.size()) {
            prescripcionesTemp.remove(indice);
            firePropertyChange("PRESCRIPCION_TEMP");
        }
    }

}
