package vista;

import control.*;
import javax.swing.*;
import java.awt.*;
import modelo.Datos;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class PanelPrincipal extends JPanel implements Notificar {
    private Datos datos;
    private Grafica grafica;
    private Controlador controlador;
    private JComboBox<Integer> tamanoCombo;
    private JTextField libreXField, libreYField;
    private JButton dibujarButton;

    public PanelPrincipal(Datos datos, Controlador controlador) {
        this.datos = datos;
        this.controlador = controlador;
        this.grafica = new Grafica(datos);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel de controles
        JPanel controles = new JPanel();
        tamanoCombo = new JComboBox<>(new Integer[]{2, 4, 8, 16, 32, 64});
        libreXField = new JTextField("0", 3);
        libreYField = new JTextField("0", 3);
        dibujarButton = new JButton("Dibujar");

        controles.add(new JLabel("Tamaño:"));
        controles.add(tamanoCombo);
        controles.add(new JLabel("Libre X:"));
        controles.add(libreXField);
        controles.add(new JLabel("Libre Y:"));
        controles.add(libreYField);
        controles.add(dibujarButton);

        add(controles, BorderLayout.NORTH);
        add(grafica, BorderLayout.CENTER);

        // Acción del botón
        dibujarButton.addActionListener(e -> {
            int tamano = (int) tamanoCombo.getSelectedItem();
            int x = Integer.parseInt(libreXField.getText());
            int y = Integer.parseInt(libreYField.getText());
            controlador.dibujar(tamano, x, y);
        });
    }

    @Override
    public void notificar(String s) {
        grafica.repaint();
    }
}
