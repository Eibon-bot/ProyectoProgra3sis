package pos.presentation.Historico;

import pos.logic.Paciente;
import pos.logic.Receta;
import pos.logic.Service;

import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    Historico view;
    Model model;

    public Controller(Historico view, Model model) throws Exception {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        model.setList(Service.instance().findAllRecetasConPrescripciones());
    }

    public void setReceta(int row) {
        Receta r = model.getList().get(row);
        model.setCurrent(r);
    }
    public void loadRecetas() throws Exception {
        model.setList(Service.instance().findAllRecetasConPrescripciones());
    }

    public void aplicarFiltros(String texto, String tipoBusqueda, String estado) {
        try {
            List<Receta> recetasFiltradas;

            Receta filtro = new Receta();
            filtro.setEstado(estado);
            recetasFiltradas = Service.instance().filtrarRecetaPorEstado(filtro);
            if (texto != null && !texto.trim().isEmpty()) {
                String textoLower = texto.toLowerCase();
                boolean esBusquedaPorNombre = tipoBusqueda != null &&
                        (tipoBusqueda.equalsIgnoreCase("Nombre del Paciente") ||
                                tipoBusqueda.equalsIgnoreCase("Nombre Paciente") ||
                                tipoBusqueda.toLowerCase().contains("nombre"));

                if (esBusquedaPorNombre) {
                    Receta filtroNombre = new Receta();
                    Paciente p = new Paciente();
                    p.setNombre(texto);
                    filtroNombre.setPaciente(p);
                    filtroNombre.setEstado(estado);

                    recetasFiltradas = Service.instance().searchRecetaPorNombrePaciente(filtroNombre);
                } else {
                    recetasFiltradas = recetasFiltradas.stream().filter(r -> {
                        switch (tipoBusqueda) {
                            case "ID Paciente":
                                return r.getPaciente() != null && r.getPaciente().getId().toLowerCase().contains(textoLower);
                            case "Código":
                                return String.valueOf(r.getCodigo()).contains(textoLower);
                            default:
                                return (r.getPaciente() != null && (
                                        r.getPaciente().getId().toLowerCase().contains(textoLower) ||
                                                (r.getPaciente().getNombre() != null && r.getPaciente().getNombre().toLowerCase().contains(textoLower))
                                ));
                        }
                    }).collect(Collectors.toList());
                }
            }
            model.setList(recetasFiltradas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}

