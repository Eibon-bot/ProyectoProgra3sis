package pos;

import pos.logic.Service;
import pos.presentation.DashBoard.ViewDB;
import pos.presentation.DespachoFarma.DespachoFarma;
import pos.presentation.Farmaceutico.Controller;
import pos.presentation.Farmaceutico.FarmaAdmin;
import pos.presentation.Historico.Historico;
import pos.presentation.Login.View.login;
import pos.presentation.Medicamentos.MedicaAdmin;
import pos.presentation.Medico.MediAdmin;
//import pos.presentation.Login.View.login;
//import pos.presentation.Login.Controller;
import pos.presentation.Farmaceutico.Model;
import pos.presentation.Pacientes.PacientesAdmin;
import pos.presentation.Prescribir.Prescribir;
//import pos.logic.Registro.AuthService;

import javax.swing.*;
import java.awt.*;




public class App {
    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
    private static final boolean DEBUG_OPEN_ADMIN_SCREENS = false;

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {


                pos.presentation.Login.Model model = new pos.presentation.Login.Model();
                login view = new login();
                Service serv= Service.instance();

                new pos.presentation.Login.Controller(view, model, serv);


                JFrame window = new JFrame("Login");
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setContentPane(view.getLogin());
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);






                if (DEBUG_OPEN_ADMIN_SCREENS) {
                    MediAdmin mView = new MediAdmin();
                    pos.presentation.Medico.Model medModel = new pos.presentation.Medico.Model();
                    new pos.presentation.Medico.Controller(mView, medModel);
                    mView.setModel(medModel);
                    mView.setController(new pos.presentation.Medico.Controller(mView, medModel));
                    medModel.setList(Service.instance().findAllMedico());
                    JFrame medWin = new JFrame("Administración de Medicos");
                    medWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    medWin.setContentPane(mView.getPanel());
                    medWin.pack();
                    medWin.setLocationRelativeTo(null);
                    medWin.setVisible(true);

                    MedicaAdmin medsView = new MedicaAdmin();
                    pos.presentation.Medicamentos.ModelMedicamentos medsModel = new pos.presentation.Medicamentos.ModelMedicamentos();
                    new pos.presentation.Medicamentos.ControllerMedicamentos(medsView, medsModel);
                    medsView.setModel(medsModel);
                    medsView.setController(new pos.presentation.Medicamentos.ControllerMedicamentos(medsView, medsModel));
                    medsModel.setList(Service.instance().findAllMedicamento());
                    JFrame medsWin = new JFrame("Administración de Medicamentos");
                    medsWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    medsWin.setContentPane(medsView.getPanel());
                    medsWin.pack();
                    medsWin.setLocationRelativeTo(null);
                    medsWin.setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación: " + e.getMessage());
            }
        });
    }




}

