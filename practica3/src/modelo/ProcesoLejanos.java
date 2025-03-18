package modelo;

import control.*;
import java.util.ArrayList;
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
        //System.out.println("Iniciando proceso Lejanos...");
        contr.getDatos().clearTiemposLejanos();
        Datos dat = contr.getDatos();
        for (int idx = 0; idx < dat.getSizeTamañosN(); idx++) {
            if (cancel) {
                System.out.println("Proceso Lejanos cancelado.");
                break;
            }
            int n = dat.getTamañoN(idx);
            ArrayList<Point2D> puntos = dat.getPuntosParaN(idx);
            //System.out.println("Procesando " + n + " puntos: " + puntos);
            long start = System.nanoTime();

            Point2D[] puntosArray = puntos.toArray(new Point2D[0]);
            double maxDistance = 0;

            for (int i = 0; i < n && !cancel; i++) {
                for (int j = i + 1; j < n && !cancel; j++) {
                    double dist = puntosArray[i].distanceTo(puntosArray[j]);
                    if (dist > maxDistance) {
                        maxDistance = dist;
                    }
                }
            }

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoLejanos(time);
                System.out.println("Lejanos n=" + n + ". Tiempo: " + time + " ns, Distancia: " + maxDistance);
                contr.notificar("Pintar");
            }
        }
        //System.out.println("Proceso Lejanos terminado.");
    }

    private void detener() {
        cancel = true;
    }

    @Override
    public synchronized void notificar(String s) {
        if (s.contentEquals("Detener")) {
            //System.out.println("Recibida orden de detener proceso Lejanos.");
            detener();
        }
    }
}