package pos.presentation.Medicamentos;

import pos.App;
import pos.logic.Medicamento;
import pos.logic.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MedicaAdmin implements PropertyChangeListener {
    private JTable tableMedicamentos;
    private JTextField codigoTextField;
    private JTextField nombreTextField;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField presentacionTextField;
    private JTextField textFieldBuscar;
    private JButton buscarButton;
    private JPanel MenuMedicamentos;
    private JPanel panellistado;
    private JComboBox BusquedaBox;
    private boolean editing = false;

    public MedicaAdmin() {




        tableMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableMedicamentos.getSelectedRow() >= 0) {
                int row = tableMedicamentos.getSelectedRow();
                controller.setMedicamento(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableMedicamentos);
        panellistado.setLayout(new BorderLayout());
        panellistado.add(scrollPane, BorderLayout.CENTER);

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validate()) return;

                Medicamento p = take();
                try {
                    p.setCodigo(Service.instance().generarNuevoCodMedicamento(nombreTextField.getText()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    if (editing) {
                        controller.update(p);
                    } else {
                        controller.create(p);
                        controller.clear();
                    }
                    JOptionPane.showMessageDialog(MenuMedicamentos, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuMedicamentos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medicamento n = take();
                    try {
                        controller.delete(n);
                        JOptionPane.showMessageDialog(MenuMedicamentos, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MenuMedicamentos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }


            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (controller == null) return;

                    String criterio = BusquedaBox.getSelectedItem() != null
                            ? BusquedaBox.getSelectedItem().toString()
                            : "Nombre";

                    String texto = textFieldBuscar.getText();

                    switch (criterio) {
                        case "Nombre":
                            controller.searchMedicamentoNombre(texto);
                            break;
                        case "Codigo":
                            controller.searchMedicamentoCodigo(texto);
                            break;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuMedicamentos,
                            ex.getMessage(),
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });

    }

    public JPanel getPanel() {
        return MenuMedicamentos;
    }

    pos.presentation.Medicamentos.ControllerMedicamentos controller;
    pos.presentation.Medicamentos.ModelMedicamentos model;

    public void setController(ControllerMedicamentos controller) {
        controller.clear();
        this.controller = controller;
    }

    public void setModel(pos.presentation.Medicamentos.ModelMedicamentos model) {
        this.model = model;
        model.addPropertyChangeListener(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelMedicamentos.LIST:
                int[] cols = {pos.presentation.Medicamentos.TableModelMedicamentos.COD,pos.presentation.Medicamentos.TableModelMedicamentos.NOMBRE, pos.presentation.Medicamentos.TableModelMedicamentos.PRESENTACION};
                tableMedicamentos.setModel(new TableModelMedicamentos(cols, model.getList()));
                break;
            case ModelMedicamentos.CURRENT:
                editing = model.getList().stream()
                        .anyMatch(p -> p.getCodigo().equals(model.current.getCodigo()));
                codigoTextField.setText(model.getCurrent().getCodigo());
                nombreTextField.setText(model.getCurrent().getNombre());
                presentacionTextField.setText(model.getCurrent().getPresentacion());
                codigoTextField.setBackground(null);
                codigoTextField.setToolTipText(null);
                nombreTextField.setBackground(null);
                nombreTextField.setToolTipText(null);
                presentacionTextField.setBackground(null);
                presentacionTextField.setToolTipText(null);
                break;
        }
        this.MenuMedicamentos.revalidate();
    }

    public Medicamento take() {
        Medicamento e = new Medicamento();
        e.setCodigo(codigoTextField.getText());
        e.setNombre(nombreTextField.getText());
        e.setPresentacion(presentacionTextField.getText());

        return e;
    }

    private boolean validate() {
        boolean valid = true;
//        if (codigoTextField.getText().isEmpty()) {
//            valid = false;
//            codigoTextField.setBackground(App.BACKGROUND_ERROR);
//            codigoTextField.setToolTipText("codigo requerido");
//        } else {
//            codigoTextField.setBackground(null);
//            codigoTextField.setToolTipText(null);
//        }

        if (nombreTextField.getText().isEmpty()) {
            valid = false;
            nombreTextField.setBackground(App.BACKGROUND_ERROR);
            nombreTextField.setToolTipText("Nombre requerido");
        } else {
            nombreTextField.setBackground(null);
            nombreTextField.setToolTipText(null);
        }

        if (presentacionTextField.getText().isEmpty()) {
            valid = false;
            presentacionTextField.setBackground(App.BACKGROUND_ERROR);
            presentacionTextField.setToolTipText("Presentación requerida");
        } else {
            presentacionTextField.setBackground(null);
            presentacionTextField.setToolTipText(null);
        }

        return valid;
    }
}
