package pos.presentation.Chat;

import pos.logic.Usuario;

import javax.swing.*;
import java.awt.event.*;

public class DialogoEnviar extends JDialog {
    private JPanel contentPane;
    private JButton buttonEnviarReal;
    private JButton buttonCancel;
    private JLabel labelNombreAEnviar;
    private JTextField textFieldMensaje;

    private Controller controller;
    private Usuario destinatario;

    public DialogoEnviar() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonEnviarReal);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonEnviarReal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSend();
            }
        });

        pack();
    }

    public void setController(Controller c){
        this.controller = c;
    }

    // Corregido: recibir Usuario en lugar de String
    public void setDestinatario(Usuario u) {
        this.destinatario = u;
        if (labelNombreAEnviar != null) {
            labelNombreAEnviar.setText(u != null && u.getId() != null ? u.getId() : "");
        }
    }

    private void doSend() {
        if (controller == null) {
            onCancel();
            return;
        }
        String mensaje = (textFieldMensaje != null) ? textFieldMensaje.getText() : null;
        if (mensaje == null || mensaje.trim().isEmpty()) {
            onCancel();
            return;
        }

        try {
            controller.enviar(mensaje);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPane,
                    "Error al enviar el mensaje: " + e.getMessage(),
                    "Error de Envío",
                    JOptionPane.ERROR_MESSAGE);
        }

        // El diálogo se cierra haya funcionado o no
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
