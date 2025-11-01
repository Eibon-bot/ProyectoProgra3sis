
package pos.presentation.Chat;

import pos.logic.Usuario;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModelConectados extends AbstractTableModel<Usuario> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int MENSAJES = 1;

    public TableModelConectados(int[] cols, List<Usuario> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }

    @Override
    protected Object getPropetyAt(Usuario e, int col) {
        switch (cols[col]) {
            case ID:
                return e == null ? "" : e.getId();
            case MENSAJES:
                // consultar al modelo global no es ideal aquí; el TableModel normalmente recibe rows y el View
                // debe actualizar la columna usando el Model. Aquí asumimos que el objeto 'e' no contiene pendientes,
                // por lo que el caller debe construir rows coincidentes con el estado actual o usar otra strategy.
                return false;
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return cols[columnIndex] == MENSAJES;
    }

    @Override
    public Class<?> getColumnClass(int colIndex) {
        if (cols[colIndex] == MENSAJES)
            return Boolean.class;
        return String.class;
    }
}
