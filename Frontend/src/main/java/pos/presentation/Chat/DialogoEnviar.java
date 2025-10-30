package pos.presentation.Chat;

import javax.swing.*;
import java.awt.event.*;

public class DialogoEnviar extends JDialog {
    private JPanel contentPane;
    private JButton buttonEnviarReal;
    private JButton buttonCancel;
    private JLabel labelNombreAEnviar;
    private JTextField textFieldMensaje;

    public DialogoEnviar() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonEnviarReal);


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonEnviarReal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public void setDestinatario(String id) {
        if (labelNombreAEnviar != null) labelNombreAEnviar.setText(id);
    }


}
