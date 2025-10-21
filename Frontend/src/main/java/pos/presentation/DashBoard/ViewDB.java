package pos.presentation.DashBoard;


import pos.logic.Medicamento;

import javax.swing.*;
import java.awt.*;

public class ViewDB {
    public JPanel panel1;
    public JComboBox<String> DesdeMes;
    public JComboBox<String> HastaMes;
    public JComboBox<Integer> DesdeAño;
    public JComboBox<Integer> HastaAño;
    public JButton aplicarRangoButton;
    public JList<Medicamento> list1;
    public JComboBox<Medicamento> comboBox3;
    public JButton aplicarSeleccionButton;
    public JPanel lineChart;
    public JPanel pieChart;
    public JButton limpiarSeleccionButton;

    public DefaultListModel<Medicamento> listModel = new DefaultListModel<>();
    private Controller controller;
    private Model model;

    public ViewDB() {
        list1.setModel(listModel);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init() throws Exception {
        comboBox3.removeAllItems();
        for (Medicamento m : model.getTodosLosMedicamentos()) {
            comboBox3.addItem(m);
        }

        DesdeMes.removeAllItems();
        HastaMes.removeAllItems();
        for (String mes : model.getMesesDisponibles()) {
            DesdeMes.addItem(mes);
            HastaMes.addItem(mes);
        }

        DesdeAño.removeAllItems();
        HastaAño.removeAllItems();
        for (Integer año : model.getAniosDisponibles()) {
            DesdeAño.addItem(año);
            HastaAño.addItem(año);
        }

        if (DesdeAño.getItemCount() > 0) DesdeAño.setSelectedIndex(0);
        if (HastaAño.getItemCount() > 0) HastaAño.setSelectedIndex(HastaAño.getItemCount() - 1);
    }

    public JPanel getPanel() {
        return panel1;
    }

    private void createUIComponents() {
        lineChart = new JPanel(new BorderLayout());
        pieChart = new JPanel(new BorderLayout());
    }
}
