package pos.presentation.Historico;

import pos.logic.Receta;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Historico implements PropertyChangeListener
{
    private JTable table1;
    private JComboBox Estado;
    private JComboBox TipoBusqueda;
    private JTextField textField1;
    private JPanel Historico;
    private JPanel panelrecetas;

    public JPanel getHistorico() {
        return Historico;
    }

    public Historico() {

        panelrecetas.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table1);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        panelrecetas.add(scrollPane, BorderLayout.CENTER);


        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscar();
            }

            private void buscar() {
                if (controller == null) return;
                String criterio = TipoBusqueda.getSelectedItem() != null ? TipoBusqueda.getSelectedItem().toString() : "Nombre del Paciente";
                String texto = textField1.getText();
                String estado = Estado.getSelectedItem() != null ? Estado.getSelectedItem().toString() : "Todos";
                controller.aplicarFiltros(texto, criterio, estado);
            }});
        Estado.addActionListener(e -> {
            if (controller == null) return;
            String criterio = TipoBusqueda.getSelectedItem() != null ? TipoBusqueda.getSelectedItem().toString() : "Nombre del Paciente";
            String texto = textField1.getText();
            String estado = Estado.getSelectedItem() != null ? Estado.getSelectedItem().toString() : "Todos";
            controller.aplicarFiltros(texto, criterio, estado);
        });

        table1.getSelectionModel().addListSelectionListener(e -> {

            int viewRow = table1.getSelectedRow();
            int modelRow = table1.convertRowIndexToModel(viewRow);

            if (modelRow < 0 || modelRow >= model.getList().size()) return;

            Receta receta = model.getList().get(modelRow);
            if (receta == null) return;

            StringBuilder info = new StringBuilder();

            if (receta.getPaciente() != null) {
                info.append("ID Paciente: ").append(receta.getPaciente().getId()).append("\n");
                info.append("Nombre Paciente: ").append(receta.getPaciente().getNombre()).append("\n");
            }
            if (receta.getMedico() != null) {
                info.append("Médico: ").append(receta.getMedico().getNombre()).append("\n");
            }

            info.append("Fecha Emisión: ").append(receta.getFechaEmision()).append("\n");
            info.append("Estado: ").append(receta.getEstado()).append("\n");
            info.append("Prescripciones: ").append("\n");
            info.append(receta.getPrescripciones());


            JOptionPane.showMessageDialog(
                    Historico.this.Historico,
                    info.toString(),
                    "Información de la Receta",
                    JOptionPane.INFORMATION_MESSAGE
            );

        });


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
            case pos.presentation.Historico.Model.LIST:
                int[] cols = {TableModel.PACIENTE_ID, TableModel.PACIENTE_NOMBRE, TableModel.MEDICO_ID, TableModel.FECHA_EMISION, TableModel.ESTADO};
                table1.setModel(new TableModel(cols, model.getList()));
                break;
        }
        this.Historico.revalidate();

    }


}



