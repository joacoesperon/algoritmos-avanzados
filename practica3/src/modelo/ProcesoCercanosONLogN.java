package modelo;

import control.*;
import modelo.Datos.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Clase ProcesoSuma que ejecuta la suma de matrices en un hilo separado.
 * 
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */
public class ProcesoCercanosONLogN extends Thread implements Notificar {
    private volatile boolean cancel;
    private Controlador contr;

    public ProcesoCercanosONLogN(Controlador c) {
        contr = c;
        cancel = false;
    }

    @Override
    public void run() {
        //System.out.println("Iniciando proceso Cercanos O(n·log n)...");
        contr.getDatos().clearTiemposCercanosONLogN();
        Datos dat = contr.getDatos();
        for (int idx = 0; idx < dat.getSizeTamañosN(); idx++) {
            if (cancel) {
                System.out.println("Proceso Cercanos O(n·log n) cancelado.");
                break;
            }
            int n = dat.getTamañoN(idx);
            ArrayList<Point2D> puntos = dat.getPuntosParaN(idx);
            //System.out.println("Procesando " + n + " puntos: " + puntos);
            long start = System.nanoTime();
            Point2D[] puntosArray = puntos.toArray(new Point2D[0]);
            Arrays.sort(puntosArray, (a, b) -> Double.compare(a.getX(), b.getX()));
            double minDistance = closestPair(puntosArray, 0, n - 1);

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoCercanosONLogN(time);
                System.out.println("O(n·log n) n=" + n + ". Tiempo: " + time + " ns, Distancia: " + minDistance);
                contr.notificar("Pintar");
            }
        }
        //System.out.println("Proceso Cercanos O(n·log n) terminado.");
    }

    private double closestPair(Point2D[] puntos, int left, int right) {
        if (right - left <= 3) {
            return bruteForce(puntos, left, right);
        }
        int mid = (left + right) / 2;
        double dl = closestPair(puntos, left, mid);
        double dr = closestPair(puntos, mid + 1, right);
        double d = Math.min(dl, dr);
        return stripClosest(puntos, left, right, mid, d);
    }

    private double bruteForce(Point2D[] puntos, int left, int right) {
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                minDist = Math.min(minDist, puntos[i].distanceTo(puntos[j]));
            }
        }
        return minDist;
    }

    private double stripClosest(Point2D[] puntos, int left, int right, int mid, double d) {
        double minDist = d;
        Point2D midPoint = puntos[mid];
        ArrayList<Point2D> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(puntos[i].getX() - midPoint.getX()) < minDist) {
                strip.add(puntos[i]);
            }
        }
        strip.sort((a, b) -> Double.compare(a.getY(), b.getY()));
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).getY() - strip.get(i).getY()) < minDist; j++) {
                minDist = Math.min(minDist, strip.get(i).distanceTo(strip.get(j)));
            }
        }
        return minDist;
    }

    private void detener() {
        cancel = true;
    }

    @Override
    public synchronized void notificar(String s) {
        if (s.contentEquals("Detener")) {
            //System.out.println("Recibida orden de detener proceso O(n·log n).");
            detener();
        }
    }
}