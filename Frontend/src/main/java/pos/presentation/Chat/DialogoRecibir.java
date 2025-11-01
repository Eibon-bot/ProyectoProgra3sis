package pos.presentation.Chat;

import javax.swing.*;
import java.awt.event.*;

public class DialogoRecibir extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelIdCambia;
    private JLabel LabelMensajeRecibido;

    private Controller controller;
    private String remitenteId;

    public DialogoRecibir() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        pack();
    }

    public void setController(Controller c){
        this.controller = c;
    }

    private void onOK() {
        if (controller != null && remitenteId != null) {
            controller.confirmReceived(remitenteId);
        }
        dispose();
    }

    public void setRemitente(String id) {
        this.remitenteId = id;
        if (labelIdCambia != null) labelIdCambia.setText(id);
    }

    public void setMensaje(String mensaje) {
        if (LabelMensajeRecibido != null) LabelMensajeRecibido.setText(mensaje);
    }
}
