/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.macotascliente.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import mx.itson.macotascliente.entities.Mascota;
import mx.itson.macotascliente.services.MascotaApiClient;

public class MascotaListado extends JFrame {

    private final MascotaApiClient api = new MascotaApiClient();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Especie", "Raza", "Edad", "Dueño"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Mascota> mascotas = new ArrayList<>();

    public MascotaListado() {
        setTitle("Mascotas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargar());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargar();
    }

    private void cargar() {
        try {
            mascotas = api.listar();
            modelo.setRowCount(0);
            for (Mascota m : mascotas) {
                modelo.addRow(new Object[]{
                    m.getId(), m.getNombre(), m.getEspecie(),
                    m.getRaza(), m.getEdad(), m.getDueno()
                });
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar con la API: " + ex.getMessage());
        }
    }

    private Mascota seleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una mascota de la tabla.");
            return null;
        }
        return mascotas.get(fila);
    }

    private void agregar() {
        MascotaForm form = new MascotaForm(this, null);
        form.setVisible(true);
        if (form.isGuardado()) {
            cargar();
        }
    }

    private void editar() {
        Mascota m = seleccionada();
        if (m == null) {
            return;
        }
        MascotaForm form = new MascotaForm(this, m);
        form.setVisible(true);
        if (form.isGuardado()) {
            cargar();
        }
    }

    private void eliminar() {
        Mascota m = seleccionada();
        if (m == null) {
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Eliminar a " + m.getNombre() + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                api.eliminar(m.getId());
                cargar();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
}