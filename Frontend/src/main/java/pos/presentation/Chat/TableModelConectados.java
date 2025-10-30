package pos.presentation.Chat;

import pos.logic.Farmaceutico;
import pos.logic.Usuario;
import pos.presentation.AbstractTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableModelConectados extends AbstractTableModel {
    public static final int ID = 0;
    public static final int MENSAJES = 1;


    private final Map<String, Boolean> marcados = new HashMap<>();

    public TableModelConectados(int[] cols, List rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }

    @Override
    protected Object getPropetyAt(Object e, int col) {
        String id = (String) e;
        switch (cols[col]) {
            case ID:        return id;
            case MENSAJES:  return marcados.getOrDefault(id, Boolean.FALSE);
            default:        return "";
        }
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return cols[columnIndex] == MENSAJES;
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (cols[columnIndex] == MENSAJES) ? Boolean.class : String.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (cols[columnIndex] == MENSAJES && aValue instanceof Boolean) {
            String id = (String) rows.get(rowIndex);
            marcados.put(id, (Boolean) aValue);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }


    public String getIdAt(int row) {
        return (row >= 0 && row < rows.size()) ? (String) rows.get(row) : null;
    }

    public java.util.List<String> getIdsMarcados() {
        java.util.List<String> out = new java.util.ArrayList<>();
        for (Object o : rows) {
            String id = (String) o;
            Boolean v = marcados.get(id);
            if (v != null && v) out.add(id);
        }
        return out;
    }
}

