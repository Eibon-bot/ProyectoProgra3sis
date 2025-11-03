
package pos.presentation.Chat;
import pos.presentation.Chat.Model;
import pos.logic.Usuario;
import pos.presentation.AbstractTableModel;

import java.util.List;

public class TableModelConectados extends AbstractTableModel<Usuario> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int MENSAJES = 1;
    private Model chatModel;

    public TableModelConectados(Model chatModel, int[] cols, List<Usuario> rows) {
        super(cols, rows);
        this.chatModel = chatModel;
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }

    @Override
    protected Object getPropetyAt(Usuario e, int col) {

        if (e == null) {
            return null;
        }

        switch (cols[col]) {
            case ID:
                return e.getId();

            case MENSAJES:
                if (this.chatModel == null) {
                    return false;
                }
                return this.chatModel.hasPending(e.getId());

            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int colIndex) {
        if (cols[colIndex] == MENSAJES)
            return Boolean.class;
        return String.class;
    }
}
