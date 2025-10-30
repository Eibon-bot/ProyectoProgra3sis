package pos.presentation.Chat;

import javax.swing.*;
import java.awt.event.*;

public class DialogoRecibir extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelIdCambia;
    private JLabel LabelMensajeRecibido;

    public DialogoRecibir() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public void setRemitente(String id) {
        if (labelIdCambia != null)
            labelIdCambia.setText(id);
    }

    public void setMensaje(String mensaje) {
        if (LabelMensajeRecibido != null)
            LabelMensajeRecibido.setText(mensaje);
    }



}
