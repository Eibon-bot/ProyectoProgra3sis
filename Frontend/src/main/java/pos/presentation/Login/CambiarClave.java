package pos.presentation.Login;

import pos.logic.Usuario;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CambiarClave extends JDialog implements PropertyChangeListener
 {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    public CambiarClave() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
    }

    private void onOK() {
        String actual = textField1.getText().trim();
        String nueva = textField2.getText().trim();
        String confirmacion = textField3.getText().trim();

        try {
            String resultado = controller.cambiarClave(actual, nueva, confirmacion);

            if (resultado == null) {
                JOptionPane.showMessageDialog(this, "Clave cambiada exitosamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + resultado);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.CURRENT:
                Usuario u = model.getCurrent();
                if (u != null) {
                    setTitle("Cambiar clave - " + u.getId());
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                } else {
                    setTitle("Cambiar clave - (sin usuario)");
                }
                break;
        }
    }


}
