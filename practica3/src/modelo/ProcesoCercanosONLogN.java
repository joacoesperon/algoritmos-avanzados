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
        contr.getDatos().clearTiemposCercanosONLogN();
        Datos dat = contr.getDatos();
        Random rand = new Random();

        for (int n : dat.getTamañosN()) {
            if (cancel) break;
            dat.generarPuntosAleatorios(n, 1000);
            long start = System.nanoTime();
            Point2D[] puntos = dat.getPuntos().toArray(new Point2D[0]);
            Arrays.sort(puntos, (a, b) -> Double.compare(a.getX(), b.getX())); // Ordenar por x
            double minDistance = closestPair(puntos, 0, n - 1);
            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoCercanosONLogN(time);
                contr.notificar("Pintar");
                System.out.println("Cercanos O(n·log n) para n=" + n + ": Tiempo(ns)=" + time + ", Distancia=" + minDistance);
            }
        }
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
            detener();
        }
    }
}