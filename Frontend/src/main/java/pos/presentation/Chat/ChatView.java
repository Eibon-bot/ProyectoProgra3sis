package pos.presentation.Chat;


import pos.logic.Usuario;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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

            if (controller != null) controller.seleccionarPorId(userId);
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
            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);
            if (userId == null || userId.isEmpty()) return;


            Usuario destinatario = findUsuarioById(userId);
            if (destinatario == null) {
                JOptionPane.showMessageDialog(panelchat, "Usuario no disponible.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DialogoEnviar dlg = new DialogoEnviar();

            dlg.setController(controller);
            dlg.setDestinatario(destinatario);
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
            String userId = (String) tm.getValueAt(modelRow, TableModelConectados.ID);
            if (userId == null || userId.isEmpty()) return;

            DialogoRecibir dlg = new DialogoRecibir();
            dlg.setController(controller);
            dlg.setRemitente(userId);


            String mensaje = "No hay mensajes";
            if (model != null) {
                List<String> pend = model.getPendingMessages(userId);
                if (pend != null && !pend.isEmpty()) {
                    mensaje = pend.get(0);
                }
            }
            dlg.setMensaje(mensaje);

            dlg.setLocationRelativeTo(panelchat);
            dlg.setVisible(true);


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

    private Usuario findUsuarioById(String id) {
        if (id == null || model == null) return null;
        for (Usuario u : model.getUsuarios()) {
            if (u != null && id.equals(u.getId())) return u;
        }
        return null;
    }




    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (panelchat == null || tableConectados == null) return;

        String prop = evt.getPropertyName();
        if (model.USUARIOS.equals(prop)) {
            tableConectados.setModel(new TableModelConectados(
                    model,
                    new int[]{TableModelConectados.ID, TableModelConectados.MENSAJES},
                    new java.util.ArrayList<>(model.getUsuarios())
            ));
        }

    }

}
