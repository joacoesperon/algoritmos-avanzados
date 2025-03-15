package modelo;

import control.*;
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
        contr.getDatos().clearTiemposCercanosON2();
        Datos dat = contr.getDatos();
        Random rand = new Random();
        
        for (int n : dat.getTamañosN()) {
            if (cancel) break;
            dat.generarPuntosAleatorios(n, 1000); // Genera n puntos en un rango de 0 a 1000
            long start = System.nanoTime();
            Point2D[] puntos = dat.getPuntos().toArray(new Point2D[0]);
            double minDistance = Double.POSITIVE_INFINITY;
            Point2D p1 = null, p2 = null;

            // Algoritmo O(n²)
            for (int i = 0; i < n && !cancel; i++) {
                for (int j = i + 1; j < n && !cancel; j++) {
                    double dist = puntos[i].distanceTo(puntos[j]);
                    if (dist < minDistance) {
                        minDistance = dist;
                        p1 = puntos[i];
                        p2 = puntos[j];
                    }
                }
            }

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoCercanosON2(time);
                contr.notificar("Pintar");
                System.out.println("Cercanos O(n²) para n=" + n + ": Tiempo(ns)=" + time + ", Distancia=" + minDistance);
            }
        }
    }

    private void detener() {
        cancel = true;
    }

    @Override
    public void notificar(String s) {
        if (s.contentEquals("Detener")) {
            detener();
        }
    }
}
