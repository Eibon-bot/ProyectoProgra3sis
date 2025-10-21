package pos.logic;

import java.io.Serializable;
import java.util.Objects;

public class Prescripcion implements Serializable {

    private int codigo;
    private Medicamento medicamento;
    private String codigoMedicamento;
    private int cantidad;
    private String indicaciones;
    private int duracion;
    private int recetaCodigo;

    public Prescripcion() {}

    public Prescripcion(int codigo, Medicamento medicamento, int cantidad, String indicaciones, int duracion) {
        this.codigo = codigo;
        this.medicamento = medicamento;
        this.codigoMedicamento = medicamento != null ? medicamento.getCodigo() : null;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracion = duracion;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
        this.codigoMedicamento = medicamento != null ? medicamento.getCodigo() : null;
    }

    public String getCodigoMedicamento() { return codigoMedicamento; }
    public void setCodigoMedicamento(String codigoMedicamento) { this.codigoMedicamento = codigoMedicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public int getRecetaCodigo() { return recetaCodigo; }
    public void setRecetaCodigo(int recetaCodigo) { this.recetaCodigo = recetaCodigo; }

    @Override
    public String toString() {
        return "[" +
                "Medicamento= " + (medicamento != null ? medicamento.getNombre() : "null") +
                ", cantidad= " + cantidad +
                ", indicaciones= " + indicaciones +
                ", duracion= " + duracion +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescripcion that = (Prescripcion) o;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}