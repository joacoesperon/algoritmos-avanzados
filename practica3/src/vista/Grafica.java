package vista;

import javax.swing.JPanel;
import modelo.Datos;
import java.awt.*;

public class Grafica extends JPanel {
    
    private Datos datos;
    private String selectedColor; // Color seleccionado para los trominos
    private static final int PANEL_SIZE = 400; // Tamaño fijo del panel en píxeles

    public Grafica(){
        this.datos = null;
        this.selectedColor = null; // Valor por defecto
    }
    
    public void setDatos(Datos nuevosDatos, String color) {
        this.datos = nuevosDatos;
        this.selectedColor = color != null ? color : "Blanco"; // Blanco por defecto si no se selecciona
        repaint(); // Solicitamos un repintado solo cuando se actualizan los datos
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (datos == null || datos.getTamano() <= 0) {
            return; // No pintamos si no hay datos válidos
        }

        // Calculamos el tamaño de cada celda para que el tablero quepa en el panel fijo
        int tamanoTablero = datos.getTamano(); // 2 o 4
        int tamanoCelda = PANEL_SIZE / tamanoTablero; // Tamaño dinámico de cada celda

        // Rellenamos todo de blanco como fondo
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);
   
        // Dibujamos el bloque libre en negro
        g.setColor(Color.BLACK);
        g.fillRect(datos.getLibreY() * tamanoCelda, datos.getLibreX() * tamanoCelda, tamanoCelda, tamanoCelda);

        // Dibujamos los trominos (celdas con el mismo número) sin bordes internos
        for (int tromino = 1; tromino <= datos.obtenerMaxTromino(); tromino++) {
            int[][] posiciones = datos.encontrarTromino(tromino);
            if (posiciones.length >= 2) { // Al menos 2 celdas para un tromino
                dibujarTrominoSinBordesInternos(g, posiciones, tamanoCelda);
            }
        }

        // Dibujamos el borde externo del tablero en gris
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, PANEL_SIZE, PANEL_SIZE);
    } 

    // Método para dibujar un tromino sin bordes internos
    private void dibujarTrominoSinBordesInternos(Graphics g, int[][] posiciones, int tamanoCelda) {
        // Ordenamos las posiciones por coordenadas para facilitar el dibujo
        java.util.Arrays.sort(posiciones, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0]; // Ordenar por fila
            return a[1] - b[1]; // Si las filas son iguales, ordenar por columna
        });
        
        // Seleccionamos el color basado en el valor de selectedColor
        Color trominoColor = Color.WHITE; // Blanco por defecto
        switch (selectedColor) {
            case "Azul":
                trominoColor = Color.BLUE;
                break;
            case "Rojo":
                trominoColor = Color.RED;
                break;
            case "Verde":
                trominoColor = Color.GREEN;
                break;
            case "Blanco":
            default:
                trominoColor = Color.WHITE;
                break;
        }
        
        // Dibujamos solo los bordes externos de las celdas del tromino
        for (int[] pos : posiciones) {
            int x = pos[1] * tamanoCelda;
            int y = pos[0] * tamanoCelda;

            g.setColor(trominoColor); // Usamos el color seleccionado para el tromino
            g.fillRect(x, y, tamanoCelda, tamanoCelda);

            // Dibujamos los bordes externos, omitiendo los internos entre celdas del mismo tromino
            boolean arriba = true, abajo = true, izquierda = true, derecha = true;

            // Verificamos si hay celdas adyacentes del mismo tromino para omitir bordes internos
            for (int[] otraPos : posiciones) {
                if (pos == otraPos) continue;

                // Si hay una celda arriba, omitimos el borde superior
                if (otraPos[0] == pos[0] - 1 && otraPos[1] == pos[1]) {
                    arriba = false;
                }
                // Si hay una celda abajo, omitimos el borde inferior
                if (otraPos[0] == pos[0] + 1 && otraPos[1] == pos[1]) {
                    abajo = false;
                }
                // Si hay una celda a la izquierda, omitimos el borde izquierdo
                if (otraPos[0] == pos[0] && otraPos[1] == pos[1] - 1) {
                    izquierda = false;
                }
                // Si hay una celda a la derecha, omitimos el borde derecho
                if (otraPos[0] == pos[0] && otraPos[1] == pos[1] + 1) {
                    derecha = false;
                }
            }

            g.setColor(Color.GRAY);
            if (arriba) g.drawLine(x, y, x + tamanoCelda, y); // Borde superior
            if (abajo) g.drawLine(x, y + tamanoCelda, x + tamanoCelda, y + tamanoCelda); // Borde inferior
            if (izquierda) g.drawLine(x, y, x, y + tamanoCelda); // Borde izquierdo
            if (derecha) g.drawLine(x + tamanoCelda, y, x + tamanoCelda, y + tamanoCelda); // Borde derecho
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PANEL_SIZE, PANEL_SIZE); // Tamaño fijo del panel
    }
}