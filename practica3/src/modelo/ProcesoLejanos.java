package modelo;

import control.*;
import java.util.ArrayList;
import java.util.Arrays;
import modelo.Datos.Point2D;

import java.util.Random;
import java.util.Stack;

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
                //System.out.println("Proceso Lejanos cancelado.");
                break;
            }
            int n = dat.getTamañoN(idx);
            ArrayList<Point2D> puntos = dat.getPuntosParaN(idx);
            //System.out.println("Procesando " + n + " puntos...");
            long start = System.nanoTime();

            // Convertir ArrayList a array para facilitar el manejo
            Point2D[] puntosArray = puntos.toArray(new Point2D[0]);
            double maxDistance = farthestPair(puntosArray);

            long time = System.nanoTime() - start;
            if (!cancel) {
                dat.addTiempoLejanos(time);
                System.out.println("Lejanos n=" + n + ": " + time + " ns, Distancia: " + maxDistance);
                contr.notificar("Pintar");
            }
        }
        //System.out.println("Proceso Lejanos terminado.");
    }

    // Método principal para encontrar la pareja más lejana
    private double farthestPair(Point2D[] points) {
        if (points.length < 2) {
            return 0.0; // No hay pareja si hay menos de 2 puntos
        }

        // Calcular el convex hull
        Point2D[] hull = convexHull(points);
        if (hull.length < 2) {
            return 0.0; // No hay pareja si el convex hull tiene menos de 2 puntos
        }

        // Encontrar el diámetro del convex hull usando rotating calipers
        return diameter(hull);
    }

    // Algoritmo de Graham's scan para calcular el convex hull
    private Point2D[] convexHull(Point2D[] points) {
        int n = points.length;
        if (n < 3) {
            return points; // Si hay menos de 3 puntos, todos están en el convex hull
        }

        // Encontrar el punto más bajo (y mínimo, x mínimo si hay empate)
        int lowest = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].getY() < points[lowest].getY() ||
                (points[i].getY() == points[lowest].getY() && points[i].getX() < points[lowest].getX())) {
                lowest = i;
            }
        }

        // Colocar el punto más bajo en la primera posición
        Point2D temp = points[0];
        points[0] = points[lowest];
        points[lowest] = temp;

        // Ordenar los puntos por ángulo polar respecto al punto más bajo
        final Point2D p0 = points[0];
        Arrays.sort(points, 1, n, (p1, p2) -> {
            int orientation = orientation(p0, p1, p2);
            if (orientation == 0) {
                return Double.compare(p0.distanceTo(p1), p0.distanceTo(p2));
            }
            return -orientation;
        });

        // Construir el convex hull usando una pila
        Stack<Point2D> hull = new Stack<>();
        hull.push(points[0]);
        hull.push(points[1]);

        for (int i = 2; i < n; i++) {
            while (hull.size() > 1) {
                Point2D top = hull.pop();
                Point2D nextToTop = hull.peek();
                if (orientation(nextToTop, top, points[i]) > 0) {
                    hull.push(top);
                    break;
                }
            }
            hull.push(points[i]);
        }

        return hull.toArray(new Point2D[0]);
    }

    // Calcular la orientación de tres puntos (p, q, r)
    // Retorna > 0 si es counterclockwise, < 0 si es clockwise, 0 si son colineales
    private int orientation(Point2D p, Point2D q, Point2D r) {
        return (q.getY() - p.getY()) * (r.getX() - q.getX()) -
               (q.getX() - p.getX()) * (r.getY() - q.getY());
    }

    // Calcular el diámetro del convex hull usando rotating calipers
    private double diameter(Point2D[] hull) {
        int n = hull.length;
        if (n < 2) {
            return 0.0;
        }
        if (n == 2) {
            return hull[0].distanceTo(hull[1]);
        }

        // Encontrar los puntos más alejados usando rotating calipers
        int k = 1;
        double maxDist = 0.0;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            while (true) {
                int nextK = (k + 1) % n;
                double dist1 = hull[i].distanceTo(hull[k]);
                double dist2 = hull[i].distanceTo(hull[nextK]);
                if (dist2 > dist1) {
                    k = nextK;
                } else {
                    break;
                }
            }
            maxDist = Math.max(maxDist, hull[i].distanceTo(hull[k]));
            maxDist = Math.max(maxDist, hull[j].distanceTo(hull[k]));
        }

        return maxDist;
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