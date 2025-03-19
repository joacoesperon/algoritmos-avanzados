package modelo;

import control.*;
import java.util.ArrayList;
import modelo.Datos.Point2D;

import java.util.Random;

/**
 * Clase ProcesoCercanoON2 que ejecuta el producto de matrices en un hilo
 * separado.
 * 
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */

public class ProcesoCercanosON2 extends Thread implements Notificar {
    private volatile boolean cancel;
    private Controlador contr;

    public ProcesoCercanosON2(Controlador c) {
        contr = c;
        cancel = false;
    }

    @Override
    public void run() {
        //System.out.println("Iniciando proceso Cercanos O(n^2)...");
        contr.getDatos().clearTiemposCercanosON2();
        Datos dat = contr.getDatos();
        for (int idx = 0; idx < dat.getSizeTamañosN(); idx++) {
            if (cancel) {
                System.out.println("Proceso Cercanos O(n^2) cancelado.");
                break;
            }
            int n = dat.getTamañoN(idx);
            ArrayList<Point2D> puntos = dat.getPuntosParaN(idx);
            //System.out.println("Procesando " + n + " puntos: " + puntos);
            long start = System.nanoTime();

            Point2D[] puntosArray = puntos.toArray(new Point2D[0]);
            double minDistance = Double.POSITIVE_INFINITY;

            for (int i = 0; i < n && !cancel; i++) {
                for (int j = i + 1; j < n && !cancel; j++) {
                    double dist = puntosArray[i].distanceTo(puntosArray[j]);
                    if (dist < minDistance) {
                        minDistance = dist;
                    }
                }
            }

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoCercanosON2(time);
                System.out.println("O(n^2) n=" + n + ": " + time + " ns, Distancia: " + minDistance);
                contr.notificar("Pintar");
            }
        }
        //System.out.println("Proceso Cercanos O(n^2) terminado.");
    }

    private void detener() {
        cancel = true;
    }

    @Override
    public void notificar(String s) {
        if (s.contentEquals("Detener")) {
            //System.out.println("Recibida orden de detener proceso O(n^2).");
            detener();
        }
    }
}
