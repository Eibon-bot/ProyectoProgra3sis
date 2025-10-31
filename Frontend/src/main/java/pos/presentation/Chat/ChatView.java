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
        // La UI aún no está lista → evita NPE
        if (panelchat == null || tableConectados == null) return;

        String prop = evt.getPropertyName();
        if (Model.USERS.equals(prop)) {
            TableModelConectados tm = ensureTableModel();      // ← siempre tendrás TM válido
            // Si tienes setRows en tu TM úsalo; si no, recrea:
            tm.setRows(model.getUsers());                      // ← si agregaste el helper
            // Si NO tienes setRows, usa esta línea en vez de la anterior:
            // tableConectados.setModel(new TableModelConectados(
            //     new int[]{TableModelConectados.ID, TableModelConectados.MENSAJES},
            //     new java.util.ArrayList<>(model.getUsers())
            // ));
        } else if (Model.INBOX.equals(prop)) {
            Mensaje m = (Mensaje) evt.getNewValue();
            TableModelConectados tm = ensureTableModel();
            // Marca “Mensajes?” = true para el remitente
            tm.setTieneMensajes(m.getFrom(), true);            // ← agrega este helper en tu TM
            // Si no tienes el helper, puedes buscar la fila y hacer setValueAt(true, fila, MENSAJES)
        }
    }






}

