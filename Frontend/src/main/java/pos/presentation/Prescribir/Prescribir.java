package pos.presentation.Prescribir;

import pos.logic.Medico;
import pos.logic.Paciente;
import pos.logic.Prescripcion;
import pos.presentation.Prescribir.DialogsPrescribir.AgregarMedicamento;
import pos.presentation.Prescribir.DialogsPrescribir.BuscarPacienteView;
import pos.presentation.Prescribir.DialogsPrescribir.ModificarDetalle;
import pos.presentation.TableModelPres;
import com.toedter.calendar.JDateChooser;
import pos.presentation.Prescribir.Model;
import pos.presentation.Prescribir.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Prescribir implements PropertyChangeListener {
    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JTable table1;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton descartarMedicamentoButton;
    private JButton detallesButton;
    private JPanel Prescribir;
    private JLabel fechaRetirolabel;
    private JLabel pacienteLabel;
    private JPanel panelfecha;
    private JPanel panellistado;
    private JDateChooser fechaRetiroChooser;

    public Prescribir() {
        buscarPacienteButton.setIcon(new ImageIcon(getClass().getResource("/pacienteBuscar.png")));
        agregarMedicamentoButton.setIcon(new ImageIcon(getClass().getResource("/medicamentosP.png")));
        fechaRetirolabel.setIcon(new ImageIcon(getClass().getResource("/calendario.png")));
        pacienteLabel.setIcon(new ImageIcon(getClass().getResource("/pacienteBuscar.png")));
        guardarButton.setIcon(new ImageIcon(getClass().getResource("/guardar.png")));
        limpiarButton.setIcon(new ImageIcon(getClass().getResource("/limpiar.png")));
        descartarMedicamentoButton.setIcon(new ImageIcon(getClass().getResource("/descartar.png")));
        detallesButton.setIcon(new ImageIcon(getClass().getResource("/lista.png")));

        fechaRetiroChooser = new JDateChooser();
        fechaRetiroChooser.setDateFormatString("dd/MM/yyyy");
        panelfecha.setLayout(new BorderLayout());
        panelfecha.add(fechaRetiroChooser, BorderLayout.CENTER);

        limpiarButton.setEnabled(false);
        descartarMedicamentoButton.setEnabled(false);
        detallesButton.setEnabled(false);


        panellistado.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table1);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        panellistado.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(guardarButton);
        panelBotones.add(limpiarButton);
        panelBotones.add(descartarMedicamentoButton);
        panelBotones.add(detallesButton);
        panellistado.add(panelBotones, BorderLayout.SOUTH);


        table1.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = table1.getSelectedRow() >= 0;
            descartarMedicamentoButton.setEnabled(filaSeleccionada);
            detallesButton.setEnabled(filaSeleccionada);
        });


        guardarButton.addActionListener(e -> {
            Paciente p = model.getCurrentPaciente();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un paciente antes de guardar la receta.");
                return;
            }
            if (fechaRetiroChooser.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fecha de retiro.");
                return;
            }

            Date date = fechaRetiroChooser.getDate();
            LocalDate fechaRetiro = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


            controller.guardarReceta(fechaRetiro);

            JOptionPane.showMessageDialog(null, "Receta guardada correctamente");
        });


        buscarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscarPacienteView dialogBuscarPaciente = new BuscarPacienteView();
                dialogBuscarPaciente.setController(controller);
                dialogBuscarPaciente.setModel(model);

                try {
                    controller.loadPacientes();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                dialogBuscarPaciente.setModal(true);
                dialogBuscarPaciente.setVisible(true);

            }
        });
        agregarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.loadMedicamentos();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                AgregarMedicamento dialogAgregarMedicamento = new AgregarMedicamento();
                dialogAgregarMedicamento.setController(controller);
                dialogAgregarMedicamento.setModel(model);

                try {
                    controller.loadPacientes();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                dialogAgregarMedicamento.setModal(true);
                dialogAgregarMedicamento.setVisible(true);

            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.limpiarPrescripciones();
            }
        });
        descartarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    controller.descartarPrescripcion(fila);
                }
            }
        });
        detallesButton.addActionListener(e -> {
            int fila = table1.getSelectedRow();
            if (fila < 0) return;
            Paciente p = model.getCurrentPaciente();
            Medico medico = model.getCurrentMedico();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "No hay información del Paciente para mostrar detalles.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nombreMedico = (medico != null) ? medico.getNombre() : "N/A (Sin sesión)";
            String idMedico = (medico != null) ? medico.getId() : "N/A";
            String especialidadMedico = (medico != null && medico instanceof Medico) ? medico.getEspecialidad() : "N/A";

            String mensaje = String.format(
                    "Médico:\n  Nombre: %s\n  ID: %s\n  Especialidad: %s\n\nPaciente:\n  Nombre: %s\n  ID: %s",
                    nombreMedico,
                    idMedico,
                    especialidadMedico,
                    p.getNombre(),
                    p.getId()
            );

            JOptionPane.showMessageDialog(null, mensaje, "Detalles de la Prescripción", JOptionPane.INFORMATION_MESSAGE);
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table1.getSelectedRow() >= 0) {
                    int fila = table1.getSelectedRow();

                    Prescripcion prescripcionEditar = model.getPrescripcionesTemp().get(fila);

                    ModificarDetalle dialog = new ModificarDetalle(prescripcionEditar);
                    dialog.setVisible(true);
                    table1.repaint();
                }
            }
        });

    }


    public JPanel getPrescribir() {
        return Prescribir;
    }

    //-------- MVC ---------
    pos.presentation.Prescribir.Controller controller;
    pos.presentation.Prescribir.Model model;

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
            case Model.PACIENTE:
                Paciente p = model.getCurrentPaciente();
                if (p != null) {
                    pacienteLabel.setText("Paciente: " + p.getNombre());
                } else {
                    pacienteLabel.setText("Paciente no seleccionado");
                }
                break;
            case "PRESCRIPCION_TEMP":
                int[] cols = {
                        TableModelPres.MEDICAMENTO,
                        TableModelPres.PRESENTACION,
                        TableModelPres.CANTIDAD,
                        TableModelPres.DURACION,
                        TableModelPres.INDICACIONES
                };
                table1.setModel(new TableModelPres(cols,model.getPrescripcionesTemp() ));

                boolean hayPrescripciones = !model.getPrescripcionesTemp().isEmpty();
                limpiarButton.setEnabled(hayPrescripciones);
                detallesButton.setEnabled(hayPrescripciones && table1.getSelectedRow() >= 0);
                descartarMedicamentoButton.setEnabled(hayPrescripciones && table1.getSelectedRow() >= 0);
                break;
        }

    }
}

