package vista;

import control.Controlador;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
            //System.out.println("Repintando gráfica...");
            paintComponent(this.getGraphics());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Datos dat = controlador.getDatos();
        int w = this.getWidth() - 1;
        int h = this.getHeight() - 24;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        g.drawLine(10, 10, 10, h - 10); // Eje Y
        g.drawLine(10, h - 10, w - 10, h - 10); // Eje X

        if (dat != null && !dat.getTamañosN().isEmpty()) {
            int maxN = dat.getTamañoN(dat.getSizeTamañosN() - 1);
            long maxTime = 1L; // Valor por defecto para evitar división por cero

            // Calcular el tiempo máximo solo con los tiempos disponibles
            if (!dat.getTiemposCercanosON2().isEmpty()) {
                maxTime = Math.max(maxTime, getMaxTime(dat.getTiemposCercanosON2()));
            }
            if (!dat.getTiemposCercanosONLogN().isEmpty()) {
                maxTime = Math.max(maxTime, getMaxTime(dat.getTiemposCercanosONLogN()));
            }
            if (!dat.getTiemposLejanos().isEmpty()) {
                maxTime = Math.max(maxTime, getMaxTime(dat.getTiemposLejanos()));
            }

            // Definir valores de referencia para el eje Y (en nanosegundos)
            long[] referenceTimes = {
                10_000_000L, 50_000_000L, 100_000_000L, 150_000_000L,
                200_000_000L, 250_000_000L, 300_000_000L, 350_000_000L
            };
            String[] referenceLabels = {
                "10M ns", "50M ns", "100M ns", "150M ns",
                "200M ns", "250M ns", "300M ns", "350M ns"
            };

            // Dibujar líneas de referencia y etiquetas en el eje Y
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < referenceTimes.length; i++) {
                long refTime = referenceTimes[i];
                if (refTime <= maxTime) {
                    int pyRef = h - 20 - (int) (refTime * (h - 40) / maxTime);
                    g.drawLine(10, pyRef, w - 10, pyRef); // Línea horizontal
                    g.setColor(Color.BLACK);
                    g.drawString(referenceLabels[i], 10, pyRef - 5); // Etiqueta a la izquierda
                    g.setColor(Color.LIGHT_GRAY);
                }
            }

            // Arreglos para almacenar las coordenadas de los puntos
            int[] xCoords = new int[dat.getSizeTamañosN()];
            int[] yCoordsON2 = new int[dat.getSizeTamañosN()];
            int[] yCoordsONLogN = new int[dat.getSizeTamañosN()];
            int[] yCoordsLejanos = new int[dat.getSizeTamañosN()];
            int countON2 = 0, countONLogN = 0, countLejanos = 0;

            // Dibujar puntos y almacenar coordenadas
            for (int i = 0; i < dat.getSizeTamañosN(); i++) {
                int n = dat.getTamañoN(i);
                int px = 10 + (n * (w - 20) / maxN); // Posición X basada en el tamaño N
                xCoords[i] = px;

                // Dibujar tiempos de Cercanos O(n^2) si existen
                if (i < dat.getSizeTiemposCercanosON2()) {
                    int pySuma = h - 20 - (int) (dat.getTiempoCercanosON2(i) * (h - 40) / maxTime);
                    g.setColor(Color.GREEN);
                    g.fillOval(px - 3, pySuma - 3, 7, 7);
                    g.setColor(Color.BLACK);
                    g.drawOval(px - 3, pySuma - 3, 7, 7);
                    yCoordsON2[countON2++] = pySuma;
                }

                // Dibujar tiempos de Cercanos O(n·log n) si existen
                if (i < dat.getSizeTiemposCercanosONLogN()) {
                    int pyLogN = h - 20 - (int) (dat.getTiempoCercanosONLogN(i) * (h - 40) / maxTime);
                    g.setColor(Color.BLUE);
                    g.fillOval(px - 3, pyLogN - 3, 7, 7);
                    g.setColor(Color.BLACK);
                    g.drawOval(px - 3, pyLogN - 3, 7, 7);
                    yCoordsONLogN[countONLogN++] = pyLogN;
                }

                // Dibujar tiempos de Lejanos si existen
                if (i < dat.getSizeTiemposLejanos()) {
                    int pyLejanos = h - 20 - (int) (dat.getTiempoLejanos(i) * (h - 40) / maxTime);
                    g.setColor(Color.RED);
                    g.fillOval(px - 3, pyLejanos - 3, 7, 7);
                    g.setColor(Color.BLACK);
                    g.drawOval(px - 3, pyLejanos - 3, 7, 7);
                    yCoordsLejanos[countLejanos++] = pyLejanos;
                }
            }

            // Dibujar líneas para Cercanos O(n^2)
            if (countON2 > 1) {
                //System.out.println("Dibujando líneas para O(n^2)...");
                g.setColor(Color.GREEN);
                for (int i = 0; i < countON2 - 1; i++) {
                    g.drawLine(xCoords[i], yCoordsON2[i], xCoords[i + 1], yCoordsON2[i + 1]);
                }
            }

            // Dibujar líneas para Cercanos O(n·log n)
            if (countONLogN > 1) {
                //System.out.println("Dibujando líneas para O(n·log n)...");
                g.setColor(Color.BLUE);
                for (int i = 0; i < countONLogN - 1; i++) {
                    g.drawLine(xCoords[i], yCoordsONLogN[i], xCoords[i + 1], yCoordsONLogN[i + 1]);
                }
            }

            // Dibujar líneas para Lejanos
            if (countLejanos > 1) {
                //System.out.println("Dibujando líneas para Lejanos...");
                g.setColor(Color.RED);
                for (int i = 0; i < countLejanos - 1; i++) {
                    g.drawLine(xCoords[i], yCoordsLejanos[i], xCoords[i + 1], yCoordsLejanos[i + 1]);
                }
            }

            // Añadir leyenda básica
            g.setColor(Color.GREEN);
            g.drawString("O(n^2)", w - 100, 20);
            g.setColor(Color.BLUE);
            g.drawString("O(n·log n)", w - 100, 40);
            g.setColor(Color.RED);
            g.drawString("Lejanos", w - 100, 60);

            // Etiquetas de ejes
            g.setColor(Color.BLACK);
            g.drawString("Cantidad de puntos", w / 2, h - 10);
            g.drawString("Tiempo (ns)", 10, 20);
        } else {
            System.out.println("No hay datos para graficar.");
        }
    }

    private long getMaxTime(ArrayList<Long> times) {
        return times.stream().mapToLong(Long::longValue).max().orElse(1L);
    }
}