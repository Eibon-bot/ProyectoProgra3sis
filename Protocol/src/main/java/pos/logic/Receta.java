package pos.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Receta implements Serializable {

    private int codigo;
    private Paciente paciente;
    private Medico medico;
    private LocalDate fechaEmision;
    private LocalDate fechaRetiro;
    private List<Prescripcion> prescripciones;
    private String estado;

    public Receta() {
        this.prescripciones = new ArrayList<>();
    }

    public Receta(int codigo, Paciente paciente, Medico medico,
                  LocalDate fechaEmision, LocalDate fechaRetiro, String estado) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaEmision = fechaEmision;
        this.fechaRetiro = fechaRetiro;
        this.prescripciones = new ArrayList<>();
        this.estado = estado;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(LocalDate fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public List<Prescripcion> getPrescripciones() { return prescripciones; }
    public void setPrescripciones(List<Prescripcion> prescripciones) { this.prescripciones = prescripciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public void agregarMedicamento(Prescripcion med) {
        prescripciones.add(med);
    }

    public String getPrescripciones2() {
        StringBuilder sb = new StringBuilder();
        for (Prescripcion p : prescripciones) {
            if (p != null) {
                sb.append(p.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idPaciente='" + (paciente != null ? paciente.getId() : "null") + '\'' +
                ", idMedico='" + (medico != null ? medico.getId() : "null") + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", medicamentos=" + prescripciones +
                ", estado='" + estado + '\'' +
                '}';
    }

    public LocalDate getFecha() {
        return getFechaEmision();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return codigo == receta.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}