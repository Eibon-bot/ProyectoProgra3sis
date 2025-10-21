package pos.presentation.Historico;

import pos.presentation.AbstractTableModel;
import pos.logic.Receta;

import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int PACIENTE_ID = 0;
    public static final int PACIENTE_NOMBRE = 1;
    public static final int MEDICO_ID = 2;
    public static final int FECHA_EMISION = 3;
    public static final int ESTADO = 4;


    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[PACIENTE_ID] = "ID Paciente";
        colNames[PACIENTE_NOMBRE] = "Nombre Paciente";
        colNames[MEDICO_ID] = "ID Médico";
        colNames[FECHA_EMISION] = "Fecha Emisión";
        colNames[ESTADO] = "Estado";

    }
    @Override
    protected Object getPropetyAt(Receta e, int col) {
        if (e == null) return "";

        switch (cols[col]) {
            case PACIENTE_ID:
                return (e.getPaciente() != null) ? e.getPaciente().getId() : "—";
            case PACIENTE_NOMBRE:
                return (e.getPaciente() != null) ? e.getPaciente().getNombre() : "—";
            case MEDICO_ID:
                return (e.getMedico() != null) ? e.getMedico().getId() : "—";
            case FECHA_EMISION:
                return (e.getFechaEmision() != null) ? e.getFechaEmision().toString() : "—";
            case ESTADO:
                return (e.getEstado() != null) ? e.getEstado() : "—";
            default:
                return "";
        }




}

}

