package vista;

import control.Controlador;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import modelo.Datos;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Grafica extends JPanel {

    private Controlador controlador;

    public Grafica(int w, int h, Controlador c) {
        controlador = c;
        this.setBounds(0, 0, w, h);
    }

    public void pintar() {
        if (this.getGraphics() != null) {
            paintComponent(this.getGraphics());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Datos dad = controlador.getDatos();
        int w = this.getWidth() - 1;
        int h = this.getHeight() - 24;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawLine(10, 10, 10, h - 10);
        g.drawLine(10, h - 10, w - 10, h - 10);
        if (dad != null) {
            int maxelement = 0;
            for (int i = 0; i < dad.getSizeElements(); i++) {
                if (dad.getElement(i) > maxelement) {
                    maxelement = dad.getElement(i);
                }
            }
            long maxtemps;
            int px, py, pax, pay;
            maxtemps = 0;
            for (int i = 0; i < dad.getSizeTiemposSuma(); i++) {
                if (dad.getTiemposSuma(i) > maxtemps) {
                    maxtemps = dad.getTiemposSuma(i);
                }
            }
            for (int i = 0; i < dad.getSizeTiemposProducto(); i++) {
                if (dad.getTiemposProducto(i) > maxtemps) {
                    maxtemps = dad.getTiemposProducto(i);
                }
            }
            // listaA
            pax = 10;
            pay = h - 10;
            for (int i = 0; i < dad.getSizeTiemposSuma(); i++) {
                g.setColor(Color.green);
                px = dad.getElement(i) * (w - 20) / maxelement;
                py = (h - 20) - ((int) (dad.getTiemposSuma(i) * (h - 40) / maxtemps));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
            // listaB
            pax = 10;
            pay = h - 10;
            for (int i = 0; i < dad.getSizeTiemposProducto(); i++) {
                g.setColor(Color.red);
                px = dad.getElement(i) * (w - 20) / maxelement;
                py = (h - 20) - ((int) (dad.getTiemposProducto(i) * (h - 40) / maxtemps));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
        }
    }
}
