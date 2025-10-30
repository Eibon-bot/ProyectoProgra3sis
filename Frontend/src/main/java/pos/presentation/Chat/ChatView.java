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
            String userId = tm.getIdAt(modelRow);

            if (userId == null || userId.isEmpty()) return;

            controller.setDestino(userId);
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
            String userId = tm.getIdAt(modelRow);

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
            String userId = tm.getIdAt(modelRow);

//            String mensaje = controller.popMensajePendiente(userId);

//            if (mensaje == null) {
//                JOptionPane.showMessageDialog(panelchat, "No hay mensajes por recibir de " + userId + ".",
//                        "Información", JOptionPane.INFORMATION_MESSAGE);
//
//                tm.setValueAt(false, modelRow, TableModelConectados.MENSAJES);
//                return;
//            }


            DialogoRecibir dlg = new DialogoRecibir();
            dlg.setRemitente(userId);
//            dlg.setMensaje(mensaje);
            dlg.setLocationRelativeTo(panelchat);
            dlg.setVisible(true);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.USERS:
//                TableModelConectados.setData(model.getUsers());
                break;
        }
    }



}

