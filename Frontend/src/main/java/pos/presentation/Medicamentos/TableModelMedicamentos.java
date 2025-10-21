package pos.presentation.Medicamentos;

import pos.presentation.AbstractTableModel;
import pos.logic.Medicamento;

import java.util.List;

public class TableModelMedicamentos extends AbstractTableModel<Medicamento> implements javax.swing.table.TableModel {
    public TableModelMedicamentos(int[] cols, List<Medicamento> rows) {
        super(cols, rows);
    }

    public static final int COD = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;
    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[COD] = "Codigo";
        colNames[NOMBRE] = "Nombre";
        colNames[PRESENTACION] = "Presentacion";
    }
    @Override
    protected Object getPropetyAt(Medicamento e, int col) {
        switch (cols[col]) {
            case COD:
                return e.getCodigo();
            case NOMBRE:
                return e.getNombre();
            case PRESENTACION:
                return e.getPresentacion();
            default:
                return "";
        }


    }

}
