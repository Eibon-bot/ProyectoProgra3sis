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
//                Model model = new Model();
//                FarmaAdmin view = new FarmaAdmin();
//                new Controller(view, model);
//
//                JFrame window = new JFrame("Farma");
//                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                window.setContentPane(view.getPanel());
//                window.pack();
//                window.setLocationRelativeTo(null);
//                window.setVisible(true);
//
//
//
//
//                Despacho.Presentation.Medico.Model modelm = new Despacho.Presentation.Medico.Model();
//                MediAdmin viewm = new MediAdmin();
//                new Despacho.Presentation.Medico.Controller(viewm, modelm);
//
//                JFrame windowm = new JFrame("Medi");
//                windowm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowm.setContentPane(viewm.getPanel());
//                windowm.pack();
//                windowm.setLocationRelativeTo(null);
//                windowm.setVisible(true);
//
//
//
//
//                Despacho.Presentation.Medicamentos.ModelMedicamentos modelme = new Despacho.Presentation.Medicamentos.ModelMedicamentos();
//                MedicaAdmin viewme = new MedicaAdmin();
//                new Despacho.Presentation.Medicamentos.ControllerMedicamentos(viewme, modelme);
//
//                JFrame windowme = new JFrame("Medicamentos");
//                windowme.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowme.setContentPane(viewme.getPanel());
//                windowme.pack();
//                windowme.setLocationRelativeTo(null);
//                windowme.setVisible(true);
//
//
//
//                Despacho.Presentation.Pacientes.Model modelp = new Despacho.Presentation.Pacientes.Model();
//                PacientesAdmin viewp = new PacientesAdmin();
//                new Despacho.Presentation.Pacientes.Controller(viewp, modelp);
//
//                JFrame windowp = new JFrame("Pacientes");
//                windowp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowp.setContentPane(viewp.getPanel());
//                windowp.pack();
//                windowp.setLocationRelativeTo(null);
//                windowp.setVisible(true);
//
//
//
//                Despacho.Presentation.Prescribir.Model modelpr = new Despacho.Presentation.Prescribir.Model();
//                Prescribir viewpr = new Prescribir();
//                new Despacho.Presentation.Prescribir.Controller(viewpr, modelpr);
//
//                JFrame windowpr = new JFrame("Prescripcion");
//                windowpr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowpr.setContentPane(viewpr.getPrescribir());
//                windowpr.pack();
//                windowpr.setLocationRelativeTo(null);
//                windowpr.setVisible(true);
//
//
//
//                Despacho.Presentation.DespachoFarma.ModelDF modeld= new Despacho.Presentation.DespachoFarma.ModelDF();
//                DespachoFarma viewd = new DespachoFarma();
//                new Despacho.Presentation.DespachoFarma.ControllerDF(modeld, viewd);
//
//                JFrame windowd = new JFrame("Despacho Farma");
//                windowd.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowd.setContentPane(viewd.getPanel1());
//                windowd.pack();
//                windowd.setLocationRelativeTo(null);
//                windowd.setVisible(true);
//
//
//
//
//                Despacho.Presentation.Historico.Model modelh= new Despacho.Presentation.Historico.Model();
//                Historico viewh = new Historico();
//                new Despacho.Presentation.Historico.Controller(viewh, modelh);
//
//                JFrame windowh = new JFrame("Historico");
//                windowh.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowh.setContentPane(viewh.getHistorico());
//                windowh.pack();
//                windowh.setLocationRelativeTo(null);
//                windowh.setVisible(true);
//
//
//
//
//                Service service= Service.instance();
//                Despacho.Presentation.DashBoard.Model modelds= new Despacho.Presentation.DashBoard.Model(service);
//                ViewDB viewds = new ViewDB();
//                new Despacho.Presentation.DashBoard.Controller(viewds, modelds);
//
//                JFrame windowds = new JFrame("Dashboard");
//                windowds.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                windowds.setContentPane(viewds.getPanel());
//                windowds.pack();
//                windowds.setLocationRelativeTo(null);
//                windowds.setVisible(true);





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

