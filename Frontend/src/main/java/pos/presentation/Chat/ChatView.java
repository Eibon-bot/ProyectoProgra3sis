package pos.presentation.Chat;


import pos.logic.Mensaje;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChatView implements PropertyChangeListener {
    private JTable tableConectados;
    private JButton buttonEnviar;
    private JButton buttonRecibir;
    private JPanel panelchat;

    public ChatView() {

        tableConectados.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int viewRow = tableConectados.getSelectedRow();
            if (viewRow < 0) return;

            int modelRow = tableConectados.convertRowIndexToModel(viewRow);

            TableModelConectados tm = (TableModelConectados) tableConectados.getModel();

            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);

            if (userId == null || userId.isEmpty()) return;

            controller.seleccionarPorId(userId);
        });




        buttonEnviar.addActionListener(e -> {
            int viewRow = tableConectados.getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(panelchat, "Seleccione un usuario.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = tableConectados.convertRowIndexToModel(viewRow);

            TableModelConectados tm = (TableModelConectados) tableConectados.getModel();
            // ✅ sin getIdAt: lee el valor de la columna ID
            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);
            if (userId == null || userId.isEmpty()) return;

            DialogoEnviar dlg = new DialogoEnviar();
            dlg.setDestinatario(userId);
            dlg.setLocationRelativeTo(panelchat);
            dlg.setVisible(true);
        });

        buttonRecibir.addActionListener(e -> {
            int viewRow = tableConectados.getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(panelchat, "Seleccione un usuario.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = tableConectados.convertRowIndexToModel(viewRow);
            TableModelConectados tm = (TableModelConectados) tableConectados.getModel();
            // ✅ sin getIdAt
            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);
            if (userId == null || userId.isEmpty()) return;

            // ❌ ya no existe popMensajePendiente: abre el diálogo directamente
            DialogoRecibir dlg = new DialogoRecibir();
            dlg.setRemitente(userId);
            // si luego integras bandeja, aquí harías dlg.setMensaje(mensaje);
            dlg.setLocationRelativeTo(panelchat);
            dlg.setVisible(true);

            // apaga la banderita "Mensajes?"
            tm.setValueAt(false, modelRow, TableModelConectados.MENSAJES);
        });



        buttonRecibir.addActionListener(e -> {
            int viewRow = tableConectados.getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(panelchat, "Seleccione un usuario.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = tableConectados.convertRowIndexToModel(viewRow);
            TableModelConectados tm = (TableModelConectados) tableConectados.getModel();
            // ✅ sin getIdAt
            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);
            if (userId == null || userId.isEmpty()) return;

            // ❌ ya no existe popMensajePendiente: abre el diálogo directamente
            DialogoRecibir dlg = new DialogoRecibir();
            dlg.setRemitente(userId);
            // si luego integras bandeja, aquí harías dlg.setMensaje(mensaje);
            dlg.setLocationRelativeTo(panelchat);
            dlg.setVisible(true);

            // apaga la banderita "Mensajes?"
            tm.setValueAt(false, modelRow, TableModelConectados.MENSAJES);
        });


    }

    public JPanel getPanelchat() {
        return panelchat;
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) throws Exception {
        this.controller=controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

    }




    private TableModelConectados ensureTableModel() {
        if (!(tableConectados.getModel() instanceof TableModelConectados)) {
            tableConectados.setModel(new TableModelConectados(
                    new int[]{TableModelConectados.ID, TableModelConectados.MENSAJES},
                    new java.util.ArrayList<>()
            ));
        }
        return (TableModelConectados) tableConectados.getModel();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (panelchat == null || tableConectados == null) return;

        String prop = evt.getPropertyName();
        if (model.USUARIOS.equals(prop)) { // <-- constante estática en la clase del modelo
            tableConectados.setModel(new TableModelConectados(
                    new int[]{TableModelConectados.ID, TableModelConectados.MENSAJES},
                    new java.util.ArrayList<>(model.getUsuarios()) // pasa la lista nueva
            ));
        }
    }








}

