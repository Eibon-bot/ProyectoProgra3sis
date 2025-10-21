package pos.presentation.Prescribir.DialogsPrescribir;

import pos.logic.Medicamento;
import pos.logic.Prescripcion;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModificarDetalle extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private Prescripcion prescripcion;
    private Medicamento med;

    public ModificarDetalle(Prescripcion prescripcion) {
        this.prescripcion = prescripcion;
        this.med = prescripcion.getMedicamento();
        setupDialog();
        setTitle("Modificar Detalle");
        cargarDatos();
    }


    public ModificarDetalle(Medicamento med) {
        this.med = med;
        setupDialog();
        setTitle("Agregar Detalle");
    }

    private void setupDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void cargarDatos() {
        if (prescripcion != null) {
            spinner1.setValue(prescripcion.getCantidad());
            spinner2.setValue(prescripcion.getDuracion());
            textField1.setText(prescripcion.getIndicaciones());
        }
    }

    private void onOK() {
        int cantidad = (Integer) spinner1.getValue();
        int duracion = (Integer) spinner2.getValue();
        String indicaciones = textField1.getText();

        if (this.prescripcion != null) {

            this.prescripcion.setCantidad(cantidad);
            this.prescripcion.setDuracion(duracion);
            this.prescripcion.setIndicaciones(indicaciones);
        } else {
            this.prescripcion = new Prescripcion(0,med, cantidad, indicaciones, duracion);
        }
        dispose();
    }

    private void onCancel() {
        this.prescripcion = null;
        dispose();
    }

    public Prescripcion getPrescripcion() {
        return prescripcion;
    }}

