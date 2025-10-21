package pos.presentation;

import pos.presentation.AbstractTableModel;
import pos.logic.Prescripcion;

import java.util.List;

public class TableModelPres extends AbstractTableModel<Prescripcion> implements javax.swing.table.TableModel {
    public TableModelPres(int[] cols, List<Prescripcion> rows) {
        super(cols, rows);
    }

    public static final int MEDICAMENTO = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int DURACION = 3;
    public static final int INDICACIONES = 4;
    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[PRESENTACION] = "Presentación";
        colNames[CANTIDAD] = "Cantidad";
        colNames[DURACION] = "Duración";
        colNames[INDICACIONES] = "Indicaciones";
    }
    @Override
    protected Object getPropetyAt(Prescripcion e, int col) {
        switch (cols[col]) {
            case MEDICAMENTO:
                return e.getMedicamento().getNombre();
            case PRESENTACION:
                return e.getMedicamento().getPresentacion();
            case CANTIDAD:
                return e.getCantidad();
            case DURACION:
                return e.getDuracion();
            case INDICACIONES:
                return e.getIndicaciones();
            default:
                return "";
        }


    }

}
