package pos.presentation.Login.View;
//
//import Despacho.Presentation.Login.CambiarClave;
//import Despacho.Presentation.Login.Controller;

import pos.logic.Usuario;
import pos.presentation.Login.CambiarClave;
import pos.presentation.Login.Controller;
import pos.presentation.Login.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class login implements PropertyChangeListener
 {
    private JPanel Login;
    private JTextField ID;
    private JPasswordField CLAVE;
    private JLabel id;
    private JLabel clave;
    private JButton ingresarButton;
    private JButton cancelarButton;
    private JButton cambiarClaveButton;
    private JLabel icono;

    public login() {
        icono.setIcon(new ImageIcon(getClass().getResource("/hospital.png")));
        ingresarButton.setIcon(new ImageIcon(getClass().getResource("/ingresar.png")));
        cancelarButton.setIcon(new ImageIcon(getClass().getResource("/cancel.png")));
        cambiarClaveButton.setIcon(new ImageIcon(getClass().getResource("/clave.png")));

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = ID.getText().trim();
                String clave = new String(CLAVE.getPassword());
                try {
                    controller.ingresar(id, clave);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }

            }
        });
        cambiarClaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        String id = ID.getText().trim();
        String clave = new String(CLAVE.getPassword());

        try {
            Usuario usuarioValidado = controller.validarUsuario(id,clave);
            model.setCurrent(usuarioValidado);
            CambiarClave dialog = new CambiarClave();
            dialog.setController(controller);
            dialog.setModel(model);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ID.setText("");
                CLAVE.setText("");
            }
        });
    }


    public JPanel getLogin() {
        return Login;
    }

    //-------- MVC ---------
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
                Usuario nuevoUsuario = model.getCurrent();
                if (nuevoUsuario != null) { // <-- clave
                    ID.setText(nuevoUsuario.getId());
                    CLAVE.setText(nuevoUsuario.getClave());
                } else {
                    ID.setText("");
                    CLAVE.setText("");
                }
                break;
        }
    }
}
