package pos.presentation.Farmaceutico;

import pos.presentation.AbstractTableModel;
import pos.logic.Farmaceutico;

import java.util.List;

public class TableModel extends AbstractTableModel<Farmaceutico> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Farmaceutico> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
    }
    @Override
    protected Object getPropetyAt(Farmaceutico e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            default:
                return "";
                }


    }

}
