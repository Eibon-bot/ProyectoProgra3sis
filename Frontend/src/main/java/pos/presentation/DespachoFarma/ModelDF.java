package pos.presentation.DespachoFarma;


import pos.presentation.AbstractModel;
import pos.logic.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ModelDF extends AbstractModel {
    public static final String PACIENTE = "paciente";
    public static final String RECETAS = "recetas";
    public static final String PACIENTES = "pacientes";

    private Paciente currentPaciente;
    private Farmaceutico currentFarmaceutico;

    private List<Paciente> listaPacientes = new ArrayList<>();
    private List<Receta> recetasPaciente;

    private PropertyChangeSupport support;

    public ModelDF() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public Paciente getCurrentPaciente() {
        return currentPaciente;
    }

    public void setCurrentPaciente(Paciente paciente) {
        this.currentPaciente = paciente;
        support.firePropertyChange(PACIENTE, null, paciente);
    }
    public Farmaceutico getCurrentFarmaceutico() {
        return currentFarmaceutico;
    }
    public void setCurrentFarmaceutico(Farmaceutico farmaceutico) {
        this.currentFarmaceutico = farmaceutico;
    }
    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }
    public void setListaPacientes(List<Paciente> pacientes) {
        this.listaPacientes = pacientes;
        support.firePropertyChange(PACIENTES, null, pacientes);
    }

    public List<Receta> getRecetasPaciente() {
        return recetasPaciente;
    }

    public void setRecetasPaciente(List<Receta> recetas) {
        this.recetasPaciente = recetas;
        support.firePropertyChange(RECETAS, null, recetas);
    }

    public void updateReceta(Receta receta) {
        support.firePropertyChange(RECETAS, null, recetasPaciente);
    }


}
