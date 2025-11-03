package pos.presentation.DespachoFarma;



import pos.logic.Paciente;
import pos.logic.Receta;
import pos.presentation.Historico.TableModel;
import pos.presentation.Prescribir.Model;
import pos.presentation.Prescribir.TableModelPacientes;
import pos.presentation.DespachoFarma.ModelDF;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DespachoFarma implements PropertyChangeListener {
    private JPanel panel1;
    private JTextField textFieldBuscarPaciente;
    private JLabel labelPaciente;
    private JTable tableRecetasPaciente;
    private JButton siguienteButton;
    private JPanel PanelPaciente;
    private JTable tablePacientes;
    private JPanel panelTablePacientes;
    private JPanel recetaspanel;


    public DespachoFarma() {
        siguienteButton.setIcon(new ImageIcon(getClass().getResource("/siguiente.png")));

        tablePacientes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablePacientes.setPreferredScrollableViewportSize(new Dimension(600, 100));
        tablePacientes.setFillsViewportHeight(true);


        JScrollPane scrollPane = new JScrollPane(tablePacientes);
        panelTablePacientes.setLayout(new BorderLayout());
        panelTablePacientes.add(scrollPane, BorderLayout.CENTER);

        tableRecetasPaciente.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableRecetasPaciente.setPreferredScrollableViewportSize(new Dimension(600, 100));
        tableRecetasPaciente.setFillsViewportHeight(true);

        JScrollPane scrollRecetas = new JScrollPane(tableRecetasPaciente);
        recetaspanel.setLayout(new BorderLayout());
        recetaspanel.add(scrollRecetas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(siguienteButton);
        recetaspanel.add(buttonPanel, BorderLayout.SOUTH);



        textFieldBuscarPaciente.getDocument().addDocumentListener(new DocumentListener() {
        private void buscar() {
            if (controller == null) return;

            String texto = textFieldBuscarPaciente.getText().trim();
            if (!texto.isEmpty()) {
                try {
                    controller.buscarPaciente(texto);
                } catch (Exception ex) {
                    labelPaciente.setText("Paciente no encontrado");
                }
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) { buscar(); }

        @Override
        public void removeUpdate(DocumentEvent e) { buscar(); }

        @Override
        public void changedUpdate(DocumentEvent e) { buscar(); }
    });
        tablePacientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablePacientes.getSelectedRow() >= 0) {
                int row = tablePacientes.getSelectedRow();
                try{controller.setPaciente(row);}
                catch(Exception ex){
                    labelPaciente.setText("Error al cargar paciente");
                }

            }
        });

        tableRecetasPaciente.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableRecetasPaciente.getSelectedRow() >= 0) {
                int row = tableRecetasPaciente.getSelectedRow();
                Receta r = model.getRecetasPaciente().get(row);

                String mensaje = "Paciente: " + (r.getPaciente() != null ? r.getPaciente().getNombre() : "N/A") + "\n"
                        + "Fecha emisión: " + r.getFechaEmision() + "\n"
                        + "Fecha: " + r.getFechaRetiro()+ "\n"
                        +"Medico: " + (r.getMedico() != null ? r.getMedico().getNombre() : "N/A") + "\n"
                        + "Estado: " + r.getEstado();
                JOptionPane.showMessageDialog(panel1, mensaje, "Detalles de la Receta", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        siguienteButton.addActionListener(e -> {
            int fila = tableRecetasPaciente.getSelectedRow();
            if (fila >= 0) {
                Receta receta = model.getRecetasPaciente().get(fila);

                controller.avanzarEstado(receta);

                tableRecetasPaciente.repaint();
            }
        });

    }

    public JPanel getPanel1() {
        return panel1;
    }









    pos.presentation.DespachoFarma.ControllerDF controller;
    pos.presentation.DespachoFarma.ModelDF model;

    public void setController(pos.presentation.DespachoFarma.ControllerDF controller) {
        this.controller = controller;
    }

    public void setModel(pos.presentation.DespachoFarma.ModelDF model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelDF.PACIENTES:
                int[] cols = {
                        pos.presentation.Prescribir.TableModelPacientes.ID,
                        pos.presentation.Prescribir.TableModelPacientes.NOMBRE,
                        pos.presentation.Prescribir.TableModelPacientes.FECHANACIMIENTO,
                        pos.presentation.Prescribir.TableModelPacientes.TELEFONO
                };
                tablePacientes.setModel(new TableModelPacientes(cols, model.getListaPacientes()));
                break;

            case Model.PACIENTE:
                Paciente p = model.getCurrentPaciente();
                if (p != null) {
                    labelPaciente.setText("Paciente: " + p.getNombre());
                } else {
                    labelPaciente.setText("Paciente no seleccionado");
                }
                break;

            case ModelDF.RECETAS:
                int[] columnas = {
                        TableModel.PACIENTE_ID,
                        TableModel.PACIENTE_NOMBRE,

                        TableModel.FECHA_EMISION,

                        TableModel.ESTADO
                };
                tableRecetasPaciente.setModel(new TableModel(columnas, model.getRecetasPaciente()));
                break;

        }}
}
