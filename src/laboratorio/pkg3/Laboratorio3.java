package laboratorio.pkg3;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import laboratorio3.vista.PantallaInicio;

public class Laboratorio3 {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }

        SwingUtilities.invokeLater(() -> new PantallaInicio().setVisible(true));
    }
}
