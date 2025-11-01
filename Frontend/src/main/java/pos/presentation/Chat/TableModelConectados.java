package pos.presentation.Chat;

import pos.logic.Farmaceutico;
import pos.logic.Usuario;
import pos.logic.UsuarioMensajes;
import pos.presentation.AbstractTableModel;


import java.util.List;

public class TableModelConectados extends AbstractTableModel<UsuarioMensajes> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int MENSAJES = 1;

    public TableModelConectados(int[] cols, List<UsuarioMensajes> rows) {
        super(cols, rows);
    }


    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }

    @Override
    protected Object getPropetyAt(UsuarioMensajes e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getNombre();
            case MENSAJES:
                return e.mensajesPendientes();
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

