package pos.presentation.Login;


import pos.logic.Administrador;
import pos.logic.Farmaceutico;
import pos.logic.Medico;
import pos.logic.Usuario;
import pos.logic.Service;
import pos.presentation.Login.View.login;
import pos.presentation.Sesion;

import javax.swing.*;

public class Controller {
    login view;
    Model model;
    Service auth;

    public Controller(login view, Model model, Service auth) {
        this.view = view;
        this.model = model;
        this.auth = auth;
        view.setController(this);
        view.setModel(model);
    }

    public void ingresar(String id, String clave) {
        try {
            Usuario u = auth.login(id, clave);
            String rol = (u.getRol() == null) ? "" : u.getRol().toLowerCase();
            Usuario usuarioActual;
            switch (rol) {
                case "medico":
                    usuarioActual = new Medico(u);
                    break;
                case "farmaceutico":
                    usuarioActual = new Farmaceutico(u);
                    break;
                case "administrador":
                    usuarioActual = new Administrador(u);
                    break;
                default:
                    usuarioActual = u;
            }
            model.setCurrent(usuarioActual);
            final Usuario usuarioFinal = usuarioActual;

            SwingUtilities.invokeLater(() -> {
                JFrame main = null;
                try {
                    main = new Sesion(usuarioFinal);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                main.setLocationRelativeTo(null);
                main.setVisible(true);
                java.awt.Window w = SwingUtilities.getWindowAncestor(view.getLogin());
                if (w != null) w.dispose();
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            model.setCurrent(null);
        }
    }

    public Usuario validarUsuario(String id, String clave) throws Exception {
        Usuario u = auth.login(id, clave);
        model.setCurrent(u);
        return u;
    }


    public String cambiarClave(String actual, String nueva, String confirmacion) throws Exception {
        Usuario u = model.getCurrent();
        if (u == null) {
            throw new Exception("No hay usuario en sesión");
        }
        if (!nueva.equals(confirmacion)) {
            throw new Exception("La nueva clave no coincide con la confirmación");
        }
        auth.cambiarClave(u.getId(), actual, nueva);

        return null;
    }


}
