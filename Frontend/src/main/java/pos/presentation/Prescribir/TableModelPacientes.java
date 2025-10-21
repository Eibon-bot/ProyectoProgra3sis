package pos.presentation.Prescribir;

import pos.presentation.AbstractTableModel;
import pos.logic.Paciente;

import java.util.List;

public class TableModelPacientes extends AbstractTableModel<Paciente> implements javax.swing.table.TableModel {
    public TableModelPacientes(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int FECHANACIMIENTO = 2;
    public static final int TELEFONO = 3;
    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[FECHANACIMIENTO] = "Fecha Nacimiento";
        colNames[TELEFONO] = "Teléfono";
    }
    @Override
    protected Object getPropetyAt(Paciente e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            case FECHANACIMIENTO:
                return e.getFechaNacimiento();
            case TELEFONO:
                return e.getTelefono();
            default:
                return "";
        }


    }

}


