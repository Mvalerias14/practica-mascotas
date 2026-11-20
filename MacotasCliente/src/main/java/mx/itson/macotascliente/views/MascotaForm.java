/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.macotascliente.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mx.itson.macotascliente.entities.Mascota;
import mx.itson.macotascliente.services.MascotaApiClient;

public class MascotaForm extends JDialog {

    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtEspecie = new JTextField(20);
    private final JTextField txtRaza = new JTextField(20);
    private final JTextField txtEdad = new JTextField(20);
    private final JTextField txtDueno = new JTextField(20);

    private final Mascota mascota;
    private final MascotaApiClient api = new MascotaApiClient();
    private boolean guardado = false;

    public MascotaForm(JFrame padre, Mascota mascota) {
        super(padre, true);
        this.mascota = mascota;
        setTitle(mascota == null ? "Agregar mascota" : "Editar mascota");
        setLayout(new BorderLayout(10, 10));

        JPanel campos = new JPanel(new GridLayout(5, 2, 8, 8));
        campos.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Especie:"));
        campos.add(txtEspecie);
        campos.add(new JLabel("Raza:"));
        campos.add(txtRaza);
        campos.add(new JLabel("Edad:"));
        campos.add(txtEdad);
        campos.add(new JLabel("Dueño:"));
        campos.add(txtDueno);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnGuardar);

        add(campos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        if (mascota != null) {
            txtNombre.setText(mascota.getNombre());
            txtEspecie.setText(mascota.getEspecie());
            txtRaza.setText(mascota.getRaza());
            txtEdad.setText(String.valueOf(mascota.getEdad()));
            txtDueno.setText(mascota.getDueno());
        }

        pack();
        setLocationRelativeTo(padre);
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String especie = txtEspecie.getText().trim();
        String dueno = txtDueno.getText().trim();

        if (nombre.isEmpty() || especie.isEmpty() || dueno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, especie y dueño son obligatorios.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero.");
            return;
        }

        try {
            Mascota m = (mascota == null) ? new Mascota() : mascota;
            m.setNombre(nombre);
            m.setEspecie(especie);
            m.setRaza(txtRaza.getText().trim());
            m.setEdad(edad);
            m.setDueno(dueno);

            if (mascota == null) {
                api.crear(m);
            } else {
                api.actualizar(m);
            }
            guardado = true;
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    public boolean isGuardado() {
        return guardado;
    }
}
