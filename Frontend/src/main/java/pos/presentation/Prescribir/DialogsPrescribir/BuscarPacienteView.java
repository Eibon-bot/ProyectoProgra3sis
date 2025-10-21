package pos.presentation.Prescribir.DialogsPrescribir;

import pos.presentation.Prescribir.Controller;
import pos.presentation.Prescribir.Model;
import pos.presentation.Prescribir.TableModelPacientes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class BuscarPacienteView extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTextField TextFieldBuscarP;
    private JTable table1;
    private JPanel panellistado;


    public BuscarPacienteView() {

        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                int row = table1.getSelectedRow();
                controller.setPaciente(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table1);
        panellistado.setLayout(new BorderLayout());
        panellistado.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Buscar Paciente");
        pack();
        setLocationRelativeTo(null);


        TextFieldBuscarP.getDocument().addDocumentListener(new DocumentListener() {
            private void buscar() throws Exception {
                if (controller == null) return;
                String criterio = comboBox1.getSelectedItem() != null ? comboBox1.getSelectedItem().toString() : "Nombre";
                String texto = TextFieldBuscarP.getText();

                switch (criterio) {
                    case "Nombre":
                        controller.searchPacienteNombre(texto);
                        break;
                    case "ID":
                        controller.searchPacienteId(texto);
                        break;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    buscar();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    buscar();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    buscar();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() >= 0) {
                    controller.setPaciente(table1.getSelectedRow());
                    SwingUtilities.getWindowAncestor(buttonOK).dispose();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscarPacienteView.this.setVisible(false);
            }
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
            case Model.LISTPACIENTE:
                int[] cols = {TableModelPacientes.ID, TableModelPacientes.NOMBRE, TableModelPacientes.FECHANACIMIENTO, TableModelPacientes.TELEFONO};
                table1.setModel(new TableModelPacientes(cols, model.getListPaciente()));
                break;
        }
        this.contentPane.revalidate();

    }

    }


