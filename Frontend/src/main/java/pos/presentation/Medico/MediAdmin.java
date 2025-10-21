package pos.presentation.Medico;

import pos.App;
import pos.logic.Medico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MediAdmin implements PropertyChangeListener {
    private JTextField textFieldIdMed;
    private JTextField textFieldNomMed;
    private JButton guardarButtonMed;
    private JButton limpiarButtonMed;
    private JTextField textFieldEspMed;
    private JButton borrarButtonMed;
    private JTextField textFieldBusqMed;
    private JButton buscarButtonMed;
    private JTable meditable;
    private JPanel MenuMedicos;
    private JPanel panellistado;
    private JComboBox BusquedaBox;
    private boolean editing = false;


    public MediAdmin() {

        meditable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && meditable.getSelectedRow() >= 0) {
                int row = meditable.getSelectedRow();
                controller.setMedico(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(meditable);
        panellistado.setLayout(new BorderLayout());
        panellistado.add(scrollPane, BorderLayout.CENTER);

        guardarButtonMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validate()) return;

                Medico p = take();
                try {
                    if (editing) {
                        controller.update(p);
                    } else {
                        controller.create(p);
                        controller.clear();
                    }
                    JOptionPane.showMessageDialog(MenuMedicos, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuMedicos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        borrarButtonMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico n = take();
                    try {
                        controller.delete(n);
                        JOptionPane.showMessageDialog(MenuMedicos, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MenuMedicos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }


            }
        });

        buscarButtonMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (controller == null) return;
                    String criterio = BusquedaBox.getSelectedItem() != null ? BusquedaBox.getSelectedItem().toString() : "Nombre";
                    String texto = textFieldBusqMed.getText();

                    switch (criterio) {
                        case "Nombre":
                            controller.searchMedicoNombre(texto);
                            break;
                        case "ID":
                            controller.searchMedicoId(texto);
                            break;
                    }

            } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuMedicos, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        limpiarButtonMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.clear();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    public JPanel getPanel() {
        return MenuMedicos;
    }

    pos.presentation.Medico.Controller controller;
    pos.presentation.Medico.Model model;

    public void setController(Controller controller) throws Exception {
        controller.clear();
        this.controller = controller;
    }

    public void setModel(pos.presentation.Medico.Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case pos.presentation.Medico.Model.LIST:
                int[] cols = {pos.presentation.Medico.TableModel.ID, pos.presentation.Medico.TableModel.NOMBRE, pos.presentation.Medico.TableModel.ESPECIALIDAD};
                meditable.setModel(new TableModel(cols,model.getList()));
                break;
            case Model.CURRENT:
                editing = model.getList().stream()
                        .anyMatch(p -> p.getId().equals(model.current.getId()));
                textFieldIdMed.setText(model.getCurrent().getId());
                textFieldNomMed.setText(model.getCurrent().getNombre());
                textFieldEspMed.setText(model.getCurrent().getEspecialidad());
                textFieldIdMed.setBackground(null);
                textFieldIdMed.setToolTipText(null);
                textFieldNomMed.setBackground(null);
                textFieldNomMed.setToolTipText(null);
                textFieldEspMed.setBackground(null);
                textFieldEspMed.setToolTipText(null);
                break;
        }
        this.MenuMedicos.revalidate();
    }

    public Medico take() {
        Medico e = new Medico();
        e.setId(textFieldIdMed.getText());
        e.setNombre(textFieldNomMed.getText());
        e.setEspecialidad(textFieldEspMed.getText());

        return e;
    }

    private boolean validate() {
        boolean valid = true;
        if (textFieldIdMed.getText().isEmpty()) {
            valid = false;
            textFieldIdMed.setBackground(App.BACKGROUND_ERROR);
            textFieldIdMed.setToolTipText("id requerido");
        } else {
            textFieldIdMed.setBackground(null);
            textFieldIdMed.setToolTipText(null);
        }

        if (textFieldNomMed.getText().isEmpty()) {
            valid = false;
            textFieldNomMed.setBackground(App.BACKGROUND_ERROR);
            textFieldNomMed.setToolTipText("Nombre requerido");
        } else {
            textFieldNomMed.setBackground(null);
            textFieldNomMed.setToolTipText(null);
        }

        if (textFieldEspMed.getText().isEmpty()) {
            valid = false;
            textFieldEspMed.setBackground(App.BACKGROUND_ERROR);
            textFieldEspMed.setToolTipText("Especialidad requerida");
        } else {
            textFieldEspMed.setBackground(null);
            textFieldEspMed.setToolTipText(null);
        }

        return valid;
    }
}
