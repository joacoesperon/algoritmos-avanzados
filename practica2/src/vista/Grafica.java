package vista;

import javax.swing.JPanel;
import modelo.Datos;
import java.awt.*;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Grafica extends JPanel {
    private Datos datos;
    private static final int TAMANO_CELDA = 50;

    public Grafica(Datos datos) {
        this.datos = datos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] tablero = datos.getTablero();
        for (int i = 0; i < datos.getTamano(); i++) {
            for (int j = 0; j < datos.getTamano(); j++) {
                if (tablero[i][j] == -1) {
                    g.setColor(Color.BLACK); // Cuadro libre
                } else if (tablero[i][j] > 0) {
                    g.setColor(new Color((tablero[i][j] * 50) % 255, (tablero[i][j] * 100) % 255, (tablero[i][j] * 150) % 255));
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * TAMANO_CELDA, i * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
                g.setColor(Color.GRAY);
                g.drawRect(j * TAMANO_CELDA, i * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(datos.getTamano() * TAMANO_CELDA, datos.getTamano() * TAMANO_CELDA);
    }
}
