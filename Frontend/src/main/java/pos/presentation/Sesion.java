package pos.presentation;

import pos.logic.Farmaceutico;
import pos.logic.Medico;
import pos.logic.Usuario;
import pos.logic.Service;
import javax.swing.JPanel;

import javax.swing.JPanel;

import pos.presentation.Medico.MediAdmin;


import pos.presentation.Farmaceutico.FarmaAdmin;


import pos.presentation.Pacientes.PacientesAdmin;


import pos.presentation.Medicamentos.MedicaAdmin;
import pos.presentation.Medicamentos.ModelMedicamentos;
import pos.presentation.Medicamentos.ControllerMedicamentos;

import pos.presentation.Prescribir.Prescribir;


import pos.presentation.DashBoard.ViewDB;


import pos.presentation.DespachoFarma.DespachoFarma;
import pos.presentation.DespachoFarma.ModelDF;
import pos.presentation.DespachoFarma.ControllerDF;

import pos.presentation.Historico.Historico;
import pos.presentation.Historico.Model;
import pos.presentation.Historico.Controller;

import pos.logic.Service;
import pos.logic.Medico;
import pos.logic.Farmaceutico;
import javax.swing.*;
import java.awt.*;

public class Sesion extends JFrame {
    private final Usuario user;

    public Sesion(Usuario user) throws Exception {
        super("Recetas - " + user.getId() + " (" + (user.getRol() == null ? "" : user.getRol().toUpperCase()) + ")");
        this.user = user;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 450));

        JTabbedPane tabs = new JTabbedPane();

        String rol = user.getRol() == null ? "" : user.getRol().toLowerCase();
        if ("administrador".equals(rol)) {
            tabs.addTab("Médicos", buildMedicosTab());
            tabs.addTab("Farmaceutas", buildFarmaceuticosTab());
            tabs.addTab("Pacientes", buildPacientesTab());
            tabs.addTab("Medicamentos", buildMedicamentosTab());
            tabs.addTab("Dashboard", buildDashboardTab());
            tabs.addTab("Histórico", buildHistoricotab());
        } else if ("medico".equals(rol)) {
            tabs.addTab("Prescribir", buildPrescribirTab());
            tabs.addTab("Histórico", buildHistoricotab());
            tabs.addTab("Dashboard", buildDashboardTab());
        } else if ("farmaceutico".equals(rol)) {
            tabs.addTab("Despacho", buildDespachoTab());
            tabs.addTab("Histórico", buildHistoricotab());
            tabs.addTab("Dashboard", buildDashboardTab());
        } else {
            tabs.add("Inicio", new JPanel());
        }

        setContentPane(tabs);
        pack();
        setLocationRelativeTo(null);
    }


    private JPanel buildChatPanelSimple() {
        // chatModel.setList(Service.instance().findUsuariosConectados());
        try {
            var chatView  = new pos.presentation.Chat.ChatView();
            var chatModel = new pos.presentation.Chat.Model();
            new pos.presentation.Chat.Controller(chatView, chatModel);
            return chatView.getPanelchat();
        } catch (Exception ex) {
            JPanel p = new JPanel();
            p.add(new JLabel("Chat no disponible"));
            return p;
        }
    }

    private JPanel withChatSidebar(JPanel center) {
        JPanel root = new JPanel(new BorderLayout());
        root.add(center, BorderLayout.CENTER);

        JPanel chat = buildChatPanelSimple();
        chat.setPreferredSize(new Dimension(260, 1));
        chat.setBorder(BorderFactory.createEmptyBorder(4,6,4,4));

        root.add(chat, BorderLayout.EAST);
        return root;
    }

    private JPanel buildMedicosTab() throws Exception {
        var view = new MediAdmin();
        var model = new pos.presentation.Medico.Model();
        new pos.presentation.Medico.Controller(view, model);
        return withChatSidebar(view.getPanel());
    }

    private JPanel buildFarmaceuticosTab() throws Exception {
        var view = new FarmaAdmin();
        var model = new pos.presentation.Farmaceutico.Model();
        new pos.presentation.Farmaceutico.Controller(view, model);
        model.setList(Service.instance().findAllFarmaceutico());
        return withChatSidebar(view.getPanel());
    }

    private JPanel buildPacientesTab() throws Exception {
        var view = new PacientesAdmin();
        var model = new pos.presentation.Pacientes.Model();
        new pos.presentation.Pacientes.Controller(view, model);
        model.setList(Service.instance().findAllPaciente());
        return withChatSidebar(view.getPanel());
    }

    private JPanel buildMedicamentosTab() throws Exception {
        var view = new MedicaAdmin();
        var model = new ModelMedicamentos();
        var controller = new ControllerMedicamentos(view, model);
        view.setModel(model);
        view.setController(controller);
        model.setList(Service.instance().findAllMedicamento());
        return withChatSidebar(view.getPanel());
    }

    private JPanel buildPrescribirTab() {
        var view = new Prescribir();
        var model = new pos.presentation.Prescribir.Model();
        new pos.presentation.Prescribir.Controller(view, model);
        model.setCurrentMedico((Medico) user);
        return withChatSidebar(view.getPrescribir());
    }

    private JPanel buildDashboardTab() throws Exception {
        var view = new ViewDB();
        var model = new pos.presentation.DashBoard.Model(Service.instance());
        var controller = new pos.presentation.DashBoard.Controller(view, model);
        view.setModel(model);
        view.setController(controller);
        view.init();
        return withChatSidebar(view.getPanel());
    }

    private JPanel buildDespachoTab() throws Exception {
        var view = new DespachoFarma();
        var model = new ModelDF();
        var controller = new ControllerDF(model, view);
        model.setCurrentFarmaceutico((Farmaceutico) user);
        controller.loadPacientes();
        return withChatSidebar(view.getPanel1());
    }

    private JPanel buildHistoricotab() throws Exception {
        var view = new Historico();
        var model = new pos.presentation.Historico.Model();
        var controller = new pos.presentation.Historico.Controller(view, model);
        controller.loadRecetas();
        return withChatSidebar(view.getHistorico());
    }
}