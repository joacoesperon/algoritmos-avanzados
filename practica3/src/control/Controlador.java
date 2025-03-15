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
    private PanelPrincipal panelPrincipal;
    private int contadorTrominos;

    public Controlador() {
        datos = new Datos();
        panelPrincipal = new PanelPrincipal(this);
        JFrame frame = new JFrame("Trominos con Backtracking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panelPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void dibujar() {
        contadorTrominos = 1;
        llenarTrominos(0, 0, datos.getTamano(), datos.getLibreX(), datos.getLibreY());
        //System.out.println("Trominos llenados, contador: " + contadorTrominos);
        datos.imprimirTablero();
        panelPrincipal.setDatos(datos); // Actualizamos los datos en la vista
        panelPrincipal.notificar("Tablero Actualizado");
    }

    public void actualizarDatos(int tamano,int libreFila, int libreColumna){
        datos.reiniciarDatos(tamano,libreFila,libreColumna);
    }
    
    // Método recursivo para llenar un tablero de tamaño tam x tam con trominos usando backtracking.
    private void llenarTrominos(int x, int y, int tam, int libreX, int libreY) {
        // Caso base: si el tamaño del subtablero es 2x2, llenamos las celdas vacías con un tromino.
        if (tam == 2) {
            // Iteramos sobre las 4 celdas del subtablero 2x2.
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    // Si la celda está vacía (valor 0), le asignamos el número del tromino actual.
                    // El bloque libre ya tiene el valor -1 y no se modifica.
                    if (datos.getTablero()[x + i][y + j] == 0) {
                        datos.getTablero()[x + i][y + j] = contadorTrominos;
                    }
                }
            }
            contadorTrominos++;
            return;
        }

        // Caso recursivo: dividimos el tablero en 4 cuadrantes.
        // Calculamos el tamaño de cada cuadrante y las coordenadas del centro.
        int mitad = tam / 2;
        int centroX = x + mitad; // Coordenada X del centro del subtablero.
        int centroY = y + mitad; // Coordenada Y del centro del subtablero.

        // Determinamos en qué cuadrante se encuentra el bloque libre y colocamos un tromino central
        // en las posiciones correspondientes para cubrir los otros cuadrantes.

        // Caso 1: El bloque libre está en el cuadrante superior izquierdo.
        if (libreX < centroX && libreY < centroY) {
            // Colocamos un tromino central que cubre las esquinas de los otros 3 cuadrantes.
            colocarTrominoCentro(centroX, centroY, centroX, centroY - 1, centroX - 1, centroY);
            // - Superior izquierdo: Contiene el bloque libre original.
            llenarTrominos(x, y, mitad, libreX, libreY);
            // - Superior derecho: Usa la celda (centroX, centroY-1) como "bloque libre".
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            // - Inferior izquierdo: Usa la celda (centroX-1, centroY) como "bloque libre".
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            // - Inferior derecho: Usa la celda (centroX, centroY) como "bloque libre".
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);

        // Caso 2: El bloque libre está en el cuadrante inferior izquierdo.
        } else if (libreX < centroX && libreY >= centroY) {
            // Colocamos un tromino central que cubre las esquinas de los otros 3 cuadrantes.
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX, centroY - 1, centroX, centroY);
            // - Superior izquierdo: Usa la celda (centroX-1, centroY-1) como "bloque libre".
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            // - Superior derecho: Usa la celda (centroX, centroY-1) como "bloque libre".
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            // - Inferior izquierdo: Contiene el bloque libre original.
            llenarTrominos(x, centroY, mitad, libreX, libreY);
            // - Inferior derecho: Usa la celda (centroX, centroY) como "bloque libre".
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);

        // Caso 3: El bloque libre está en el cuadrante superior derecho.
        } else if (libreX >= centroX && libreY < centroY) {
            // Colocamos un tromino central que cubre las esquinas de los otros 3 cuadrantes.
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX - 1, centroY, centroX, centroY);
            // - Superior izquierdo: Usa la celda (centroX-1, centroY-1) como "bloque libre".
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            // - Superior derecho: Contiene el bloque libre original.
            llenarTrominos(centroX, y, mitad, libreX, libreY);
            // - Inferior izquierdo: Usa la celda (centroX-1, centroY) como "bloque libre".
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            // - Inferior derecho: Usa la celda (centroX, centroY) como "bloque libre".
            llenarTrominos(centroX, centroY, mitad, centroX, centroY);

        // Caso 4: El bloque libre está en el cuadrante inferior derecho.
        } else {
            // Colocamos un tromino central que cubre las esquinas de los otros 3 cuadrantes.
            colocarTrominoCentro(centroX - 1, centroY - 1, centroX - 1, centroY, centroX, centroY - 1);
            // - Superior izquierdo: Usa la celda (centroX-1, centroY-1) como "bloque libre".
            llenarTrominos(x, y, mitad, centroX - 1, centroY - 1);
            // - Superior derecho: Usa la celda (centroX, centroY-1) como "bloque libre".
            llenarTrominos(centroX, y, mitad, centroX, centroY - 1);
            // - Inferior izquierdo: Usa la celda (centroX-1, centroY) como "bloque libre".
            llenarTrominos(x, centroY, mitad, centroX - 1, centroY);
            // - Inferior derecho: Contiene el bloque libre original.
            llenarTrominos(centroX, centroY, mitad, libreX, libreY);
        }
    }

    public void colocarTrominoCentro(int x1, int y1, int x2, int y2, int x3, int y3) {
        datos.setBloque(x1,y1,contadorTrominos);
        datos.setBloque(x2,y2,contadorTrominos);
        datos.setBloque(x3,y3,contadorTrominos);
        contadorTrominos++;
    }

    @Override
    public void notificar(String s) {
        if(s.startsWith("Dibujar")){
            dibujar();   
        }       
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controlador::new);
    }
}