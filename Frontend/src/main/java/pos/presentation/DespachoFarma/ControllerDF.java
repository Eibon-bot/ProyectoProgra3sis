package pos.presentation.DespachoFarma;


import pos.logic.Paciente;
import pos.logic.Receta;
import pos.logic.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControllerDF {
    private ModelDF model;
    private DespachoFarma view;

    public ControllerDF(ModelDF model, DespachoFarma view) throws Exception {
        this.model = model;
        this.view = view;

        this.view.setController(this);
        this.view.setModel(model);

        model.setListaPacientes(Service.instance().findAllPaciente());
    }
    public void setPaciente(int row)throws Exception {
        Paciente p = model.getListaPacientes().get(row);

        Receta filtro = new Receta();
        filtro.setPaciente(p);

        List<Receta> recetas = Service.instance().searchRecetaPorIdPaciente(filtro);

        model.setCurrentPaciente(p);
        model.setRecetasPaciente(recetas);
    }





    public void buscarPaciente(String id) throws Exception {
        Paciente criterio = new Paciente();
        criterio.setId(id);

        List<Paciente> resultados = Service.instance().searchPacienteId(criterio);
        model.setListaPacientes(resultados);

        if (!resultados.isEmpty()) {
            Paciente p = resultados.get(0);
            model.setCurrentPaciente(p);

            Receta filtro = new Receta();
            filtro.setPaciente(p);

            model.setRecetasPaciente(Service.instance().searchRecetaPorIdPaciente(filtro));
        } else {
            model.setCurrentPaciente(null);
            model.setRecetasPaciente(new ArrayList<>());
        }
    }


    public void loadPacientes() throws Exception {
        model.setListaPacientes(Service.instance().findAllPaciente());
    }

    public boolean puedeProcesar(Receta receta) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaRetiro = receta.getFechaRetiro();

        return !hoy.isBefore(fechaRetiro.minusDays(3)) && !hoy.isAfter(fechaRetiro.plusDays(3));
    }



    public boolean avanzarEstado(Receta receta) {

        if (!puedeProcesar(receta)) {
            return false;
        }


        switch (receta.getEstado()) {
            case "Confeccionada":
                receta.setEstado("Proceso");
                break;
            case "Proceso":
                receta.setEstado("Lista");
                break;
            case "Lista":
                receta.setEstado("Entregada");
                break;
            case "Entregada":
                JOptionPane.showMessageDialog(null,
                        "La receta ya está entregada.");
                return false;
        }
        try {
            Service.instance().updateReceta(receta);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar en BD: " + ex.getMessage());
            return false;
        }




        model.updateReceta(receta);
        return true;
    }


}
