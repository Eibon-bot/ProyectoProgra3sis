package pos.presentation.Prescribir.DialogsPrescribir;

import pos.logic.Medicamento;
import pos.logic.Prescripcion;
import pos.logic.Receta;
import pos.presentation.Medicamentos.TableModelMedicamentos;
import pos.presentation.Prescribir.Controller;
import pos.presentation.Prescribir.Model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AgregarMedicamento extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTextField textFieldBuscar;
    private JTable table1;
    private JPanel panellistado;
    private Prescripcion prescripcionSeleccionada;

    public Prescripcion getPrescripcionSeleccionada() {
        return prescripcionSeleccionada;
    }


    public AgregarMedicamento() {
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                int row = table1.getSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table1);
        panellistado.setLayout(new BorderLayout());
        panellistado.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Agregar Medicamento");
        pack();
        setLocationRelativeTo(null);

        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                Medicamento med = model.getListaMedicamentos().get(table1.getSelectedRow());
                model.setCurrentMedicamento(med);

                if (model.getCurrentReceta() == null) {
                    model.setCurrentReceta(new Receta());
                }

                ModificarDetalle dialog = new ModificarDetalle(med);
                dialog.setModal(true);
                dialog.setVisible(true);

                Prescripcion nueva = dialog.getPrescripcion();
                if (nueva != null) {
                    model.agregarPrescripcionTemp(nueva);
                }

            }
        });



        textFieldBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() throws Exception {
                if (controller == null) return;
                String criterio = comboBox1.getSelectedItem().toString();
                String texto = textFieldBuscar.getText();

                switch (criterio) {
                    case "Nombre":
                        controller.searchMedicamentoNombre(texto);
                        break;
                    case "Código":
                        controller.searchMedicamentoCod(texto);
                        break;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    filter();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    filter();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    filter();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() >= 0) {
                    controller.setMedicamento(table1.getSelectedRow());
//                    SwingUtilities.getWindowAncestor(buttonOK).dispose();
                    dispose();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarMedicamento.this.setVisible(false);
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
            case Model.LISTMEDICAMENTO:
                int[] cols = {TableModelMedicamentos.COD, TableModelMedicamentos.NOMBRE, TableModelMedicamentos.PRESENTACION};
                table1.setModel(new TableModelMedicamentos(cols, model.getListaMedicamentos()));
                break;
        }
        this.contentPane.revalidate();

    }

}

