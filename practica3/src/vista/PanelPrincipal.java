package vista;

import control.Controlador;
import control.Notificar;
import javax.swing.*;
import java.awt.*;
import modelo.Datos;

public class PanelPrincipal extends JPanel implements Notificar {
    private Datos datos;
    private Grafica grafica;
    private Controlador controlador;
    private JComboBox<Integer> tamanoCombo;
    private JButton dibujarButton;
    private JTextField libreFilaField;
    private JTextField libreColumnaField;
    private JComboBox<String> colorCombo; // Nuevo combo box para colores
    
    public PanelPrincipal(Controlador controlador) {
        this.datos = null;
        this.controlador = controlador;
        this.grafica = new Grafica();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel de controles
        JPanel controles = new JPanel();
        tamanoCombo = new JComboBox<>(new Integer[]{4, 8, 16, 32, 64, 128});
        dibujarButton = new JButton("Dibujar");
        libreFilaField = new JTextField(5); // Campo para LibreX
        libreColumnaField = new JTextField(5); // Campo para LibreY
        colorCombo = new JComboBox<>(new String[]{"Blanco", "Azul", "Rojo", "Verde"}); // Combo box para colores
        colorCombo.setSelectedIndex(0); // Blanco como valor por defecto

        // Añadimos etiquetas y campos de texto
        controles.add(new JLabel("Tamaño:"));
        controles.add(tamanoCombo);
        controles.add(new JLabel("LibreFila:"));
        controles.add(libreFilaField);
        controles.add(new JLabel("LibreColumna:"));
        controles.add(libreColumnaField);
        controles.add(new JLabel("Color:"));
        controles.add(colorCombo); // Añadimos el combo box de colores
        controles.add(dibujarButton);

        // Panel intermedio para centrar la gráfica
        JPanel graficaPanel = new JPanel();
        graficaPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrado horizontal
        graficaPanel.add(grafica);

        add(controles, BorderLayout.NORTH);
        add(graficaPanel, BorderLayout.CENTER); // Añadimos el panel intermedio

        dibujarButton.addActionListener(e -> {
            int tamano = (int) tamanoCombo.getSelectedItem();
            int libreFila, libreColumna;

            // Validación de los campos de texto
            try {
                libreFila = Integer.parseInt(libreFilaField.getText().trim());
                libreColumna = Integer.parseInt(libreColumnaField.getText().trim());

                // Verificación de rango
                if (libreFila < 0 || libreFila >= tamano || libreColumna < 0 || libreColumna >= tamano) {
                    JOptionPane.showMessageDialog(this, "Las coordenadas (LibreX, LibreY) deben estar entre 0 y " + (tamano - 1) + ".", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos para LibreX y LibreY.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si la validación pasa, actualizamos el controlador
            controlador.actualizarDatos(tamano, libreFila, libreColumna);
            controlador.notificar("Dibujar");
        });
    }

    public void setDatos(Datos nuevosDatos) {
        this.datos = nuevosDatos;
    }
    
    @Override
    public void notificar(String s) {
        if(s.startsWith("Tablero Actualizado")){
            grafica.setDatos(datos, (String) colorCombo.getSelectedItem()); // Aseguramos que el color se aplique 
        }               
    }
}