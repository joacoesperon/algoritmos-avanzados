package modelo;

import control.*;
import modelo.Datos.Point2D;

import java.util.Random;

/**
 * Clase ProcesoCercanoONLogN que ejecuta la suma de matrices en un hilo separado.
 * 
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */
public class ProcesoLejanos extends Thread implements Notificar {

    private volatile boolean cancel;
    private Controlador contr;

    public ProcesoLejanos(Controlador c) {
        contr = c;
        cancel = false;
    }

    @Override
    public void run() {        
        contr.getDatos().clearTiemposLejanos();
        Datos dat = contr.getDatos();
        Random rand = new Random();

        for (int n : dat.getTamañosN()) {
            if (cancel) break;
            dat.generarPuntosAleatorios(n, 1000);
            long start = System.nanoTime();
            Point2D[] puntos = dat.getPuntos().toArray(new Point2D[0]);
            double maxDistance = 0;
            Point2D p1 = null, p2 = null;

            // Algoritmo simplificado: buscar la distancia máxima entre todos los pares
            for (int i = 0; i < n && !cancel; i++) {
                for (int j = i + 1; j < n && !cancel; j++) {
                    double dist = puntos[i].distanceTo(puntos[j]);
                    if (dist > maxDistance) {
                        maxDistance = dist;
                        p1 = puntos[i];
                        p2 = puntos[j];
                    }
                }
            }

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoLejanos(time);
                contr.notificar("Pintar");
                System.out.println("Lejanos para n=" + n + ": Tiempo(ns)=" + time + ", Distancia=" + maxDistance);
            }
        }
    }

    private void detener() {
         cancel = true; 
    }

    @Override
    public synchronized void notificar(String s) {
        if (s.contentEquals("Detener")) {
            detener();
        }
    }

}