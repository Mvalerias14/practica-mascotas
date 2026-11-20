/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mx.itson.macotascliente;

import javax.swing.SwingUtilities;
import mx.itson.macotascliente.views.MascotaListado;

public class MacotasCliente {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MascotaListado().setVisible(true));
    }
}