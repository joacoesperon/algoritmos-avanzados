package control;

import javax.swing.JFrame;
import modelo.*;
import vista.PanelPrincipal;
import javax.swing.*;

/**
 * Controlador principal que implementa el patrón MVC y gestiona la comunicación
 * entre la vista y el modelo.
 *
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */
public class Controlador implements Notificar {
    private Datos datos;
    private PanelPrincipal vista;
    private int contadorTrominos; // Para asignar un número a cada tromino

    public Controlador() {
        datos = new Datos(2); // Tamaño inicial por defecto
        vista = new PanelPrincipal(datos, this);
        JFrame frame = new JFrame("Trominos con Backtracking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(vista);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        frame.setVisible(true);
    }

    public void dibujar(int tamano, int libreX, int libreY) {
        datos = new Datos(tamano);
        datos.setLibre(libreX, libreY);
        datos.reiniciarTablero();
        datos.getTablero()[libreX][libreY] = -1; // Cuadro libre
        contadorTrominos = 1;
        llenarTrominos(0, 0, tamano, libreX, libreY);
        notificar("Tablero actualizado");
    }

    private void llenarTrominos(int x, int y, int tam, int libreX, int libreY) {
        if (tam == 2) {
            // Caso base: tablero 2x2
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (datos.getTablero()[x + i][y + j] == 0) {
                        datos.getTablero()[x + i][y + j] = contadorTrominos;
                    }
                }
            }
            contadorTrominos++;
            return;
        }

        int mitad = tam / 2;
        int centroX = x + mitad;
        int centroY = y + mitad;

        // Determinar en qué cuadrante está el cuadro libre
        if (libreX < centroX && libreY < centroY) {
            // Cuadrante superior izquierdo
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX, centroY - 1, centroX - 1, centroY);
            llenarTrominos(x, y, mitad, libreX, libreY);
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);
        } else if (libreX < centroX && libreY >= centroY) {
            // Cuadrante inferior izquierdo
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX, centroY - 1, centroX, centroY);
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            llenarTrominos(x, centroY, mitad, libreX, libreY);
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);
        } else if (libreX >= centroX && libreY < centroY) {
            // Cuadrante superior derecho
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX - 1, centroY, centroX, centroY);
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            llenarTrominos(centroX, y, mitad, libreX, libreY);
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);
        } else {
            // Cuadrante inferior derecho
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX - 1, centroY, centroX, centroY - 1);
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            llenarTrominos(centroX, centroY, mitad, libreX, libreY);
        }
    }

    private void colocarTrominoCentro(int x1, int y1, int x2, int y2, int x3, int y3) {
        datos.getTablero()[x1][y1] = contadorTrominos;
        datos.getTablero()[x2][y2] = contadorTrominos;
        datos.getTablero()[x3][y3] = contadorTrominos;
        contadorTrominos++;
    }

    @Override
    public void notificar(String s) {
        vista.notificar(s);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controlador::new);
    }
}
