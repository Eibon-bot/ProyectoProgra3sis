package pos.presentation.DashBoard;

import pos.logic.Medicamento;
import pos.logic.Prescripcion;
import pos.logic.Receta;
import pos.logic.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private final Service service;

    public Model(Service service) {
        this.service = service;
    }

    public List<Medicamento> getTodosLosMedicamentos() throws Exception {
        return service.findAllMedicamento();
    }

    public List<Receta> getRecetasEntreFechas(LocalDate desde, LocalDate hasta) throws Exception {

        return service.findAllRecetasConPrescripciones().stream()
                .filter(r -> r.getFechaEmision() != null &&
                        !r.getFechaEmision().isBefore(desde) &&
                        !r.getFechaEmision().isAfter(hasta))
                .collect(Collectors.toList());
    }


    public Map<String, Map<String, Integer>> getCantidadMedicamentosPorMes(
            List<Medicamento> seleccionados, LocalDate desde, LocalDate hasta) throws Exception {

        Map<String, Map<String, Integer>> resultado = new LinkedHashMap<>();

        for (Medicamento m : seleccionados) {
            Map<String, Integer> porMes = new TreeMap<>();

            for (Receta r : getRecetasEntreFechas(desde, hasta)) {
                for (Prescripcion p : r.getPrescripciones()) {
                    if (p.getMedicamento() != null &&
                            p.getMedicamento().getCodigo().equals(m.getCodigo())) {

                        LocalDate fecha = r.getFechaEmision();
                        String mes = fecha.getYear() + "-" + fecha.getMonthValue();
                        porMes.put(mes, porMes.getOrDefault(mes, 0) + p.getCantidad());
                    }
                }
            }

            resultado.put(m.getNombre(), porMes);
        }

        return resultado;
    }


    public Map<String, Long> getEstadosRecetas(LocalDate desde, LocalDate hasta) throws Exception {
        return getRecetasEntreFechas(desde, hasta).stream()
                .collect(Collectors.groupingBy(Receta::getEstado, Collectors.counting()));
    }

    public List<String> getMesesDisponibles() {
        return Arrays.stream(Month.values())
                .map(m -> m.getValue() + "-" + m.name().substring(0, 1) + m.name().substring(1).toLowerCase())
                .collect(Collectors.toList());
    }

    public List<Integer> getAniosDisponibles() throws Exception {
        return service.findAllRecetasConPrescripciones().stream()
                .map(r -> r.getFechaEmision().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
