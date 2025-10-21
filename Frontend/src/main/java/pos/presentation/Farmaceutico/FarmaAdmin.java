package pos.presentation.Farmaceutico;

import pos.App;
import pos.logic.Farmaceutico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FarmaAdmin implements PropertyChangeListener {
    private JLabel idlabelFarma;
    private JTextField textFieldIdFarma;
    private JLabel nombreFarma;
    private JTextField textFieldNomFarma;
    private JButton guardarButtonFarma;
    private JButton limpiarButtonFarma;
    private JButton borrarButtonFarma;
    private JTextField textFieldBusqFarma;
    private JButton buscarButtonFarma;
    private JPanel MenuFarmaceutas;
    private JTable farmatable;
    private JPanel panellistado;
    private JComboBox BusquedaBox;
    private boolean editing = false;

    public FarmaAdmin() {

        buscarButtonFarma.setIcon(new ImageIcon(getClass().getResource("/pacienteBuscar.png")));
        guardarButtonFarma.setIcon(new ImageIcon(getClass().getResource("/guardar.png")));
        limpiarButtonFarma.setIcon(new ImageIcon(getClass().getResource("/limpiar.png")));
        borrarButtonFarma.setIcon(new ImageIcon(getClass().getResource("/descartar.png")));

        farmatable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && farmatable.getSelectedRow() >= 0) {
                int row = farmatable.getSelectedRow();
                controller.setFarmaceutico(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(farmatable);
        panellistado.setLayout(new BorderLayout());
        panellistado.add(scrollPane, BorderLayout.CENTER);

        guardarButtonFarma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validate()) return;

                Farmaceutico p = take();
                try {
                    if (editing) {
                        controller.update(p);
                    } else {
                        controller.create(p);
                        controller.clear();
                    }
                    JOptionPane.showMessageDialog(MenuFarmaceutas, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuFarmaceutas, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        borrarButtonFarma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textFieldIdFarma.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(MenuFarmaceutas, "Id requerido para borrar", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    Farmaceutico f = new Farmaceutico();
                    f.setId(id);
                    controller.delete(f);
                    JOptionPane.showMessageDialog(MenuFarmaceutas, "REGISTRO ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuFarmaceutas, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }




            }
        });

        buscarButtonFarma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (controller == null) return;

                    String criterio = BusquedaBox.getSelectedItem() != null
                            ? BusquedaBox.getSelectedItem().toString()
                            : "Nombre";

                    String texto = textFieldBusqFarma.getText();

                    switch (criterio) {
                        case "Nombre":
                            controller.searchFarmaceuticoNombre(texto);
                            break;
                        case "ID":
                            controller.searchFarmaceuticoId(texto);
                            break;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MenuFarmaceutas,
                            ex.getMessage(),
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        limpiarButtonFarma.addActionListener(new ActionListener() {
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
        return MenuFarmaceutas;
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) throws Exception {
        controller.clear();
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID,TableModel.NOMBRE};
                farmatable.setModel(new TableModel(cols,model.getList()));
                break;
            case Model.CURRENT:
                editing = model.getList().stream()
                        .anyMatch(p -> p.getId().equals(model.current.getId()));
                textFieldIdFarma.setText(model.getCurrent().getId());
                textFieldNomFarma.setText(model.getCurrent().getNombre());
                textFieldIdFarma.setBackground(null);
                textFieldIdFarma.setToolTipText(null);
                textFieldNomFarma.setBackground(null);
                textFieldNomFarma.setToolTipText(null);
                break;


        }
        this.MenuFarmaceutas.revalidate();
    }

    public Farmaceutico take() {
        Farmaceutico e = new Farmaceutico();
        e.setId(textFieldIdFarma.getText());
        e.setNombre(textFieldNomFarma.getText());

        return e;
    }

    private boolean validate() {
        boolean valid = true;
        if (textFieldIdFarma.getText().isEmpty()) {
            valid = false;
            textFieldIdFarma.setBackground(App.BACKGROUND_ERROR);
            textFieldIdFarma.setToolTipText("id requerido");
        } else {
            textFieldIdFarma.setBackground(null);
            textFieldIdFarma.setToolTipText(null);
        }

        if (textFieldNomFarma.getText().isEmpty()) {
            valid = false;
            textFieldNomFarma.setBackground(App.BACKGROUND_ERROR);
            textFieldNomFarma.setToolTipText("Nombre requerido");
        } else {
            textFieldNomFarma.setBackground(null);
            textFieldNomFarma.setToolTipText(null);
        }

        return valid;
    }
}