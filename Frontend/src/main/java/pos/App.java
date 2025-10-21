package pos;

import pos.logic.Service;
import pos.presentation.Login.Model;
import pos.presentation.Login.View.login;
import pos.presentation.Login.Controller;
import pos.presentation.Login.View.login;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
    public static JFrame window;
    public static JTabbedPane tabbedPane;

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;
    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                window = new JFrame("POS: Point Of Sale");
                tabbedPane = new JTabbedPane();
                window.setContentPane(tabbedPane);

                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        Service.instance().stop(); // desconectar del servidor
                    }
                });

                Model loginModel = new Model();
                login loginView = new login();
                Controller loginController = new Controller(loginView, loginModel, Service.instance());


                tabbedPane.addTab("Login", null, loginView.getLogin());


                window.setSize(800, 500);
                window.setResizable(false);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al iniciar el cliente: " + e.getMessage());
            }
        });
    }


    public static void addTab(String title, Icon icon, JPanel panel) {
        tabbedPane.addTab(title, icon, panel);
    }
}