package modelo;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Datos {
    public static final int LIMITE = 10000; // Límite para x, y de los puntos

    private ArrayList<Integer> tamañosN;
    private ArrayList<Long> tiemposCercanosON2;
    private ArrayList<Long> tiemposCercanosONLogN;
    private ArrayList<Long> tiemposLejanos;
    private ArrayList<ArrayList<Point2D>> puntosPorN;

    public Datos() {
        tamañosN = new ArrayList<>();
        tiemposCercanosON2 = new ArrayList<>();
        tiemposCercanosONLogN = new ArrayList<>();
        tiemposLejanos = new ArrayList<>();
        puntosPorN = new ArrayList<>();
    }

    public void clear() {
        //System.out.println("Limpiando todos los datos...");
        tamañosN.clear();
        tiemposCercanosON2.clear();
        tiemposCercanosONLogN.clear();
        tiemposLejanos.clear();
        puntosPorN.clear();
    }

    public void preparar() {
        //System.out.println("Generando puntos aleatorios para tamaños N...");
        Random rand = new Random();
        for (int n = 1000; n <= 10000; n += 1000) {
            //System.out.println("Generando " + n + " puntos...");
            this.addTamañoN(n);
            ArrayList<Point2D> puntos = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int x = rand.nextInt(LIMITE);
                int y = rand.nextInt(LIMITE);
                puntos.add(new Point2D(x, y));
            }
            this.addPuntosParaN(puntos);
            //System.out.println(n + " puntos generados con éxito.");
        }
        //System.out.println("Generación de puntos completada.");
    }

    public void clearTiemposCercanosON2() {
        //System.out.println("Limpiando tiempos de Cercanos O(n^2)...");
        tiemposCercanosON2.clear();
    }

    public void clearTiemposCercanosONLogN() {
        //System.out.println("Limpiando tiempos de Cercanos O(n·log n)...");
        tiemposCercanosONLogN.clear();
    }

    public void clearTiemposLejanos() {
        //System.out.println("Limpiando tiempos de Lejanos...");
        tiemposLejanos.clear();
    }

    public void clearTamañosN() {
        //System.out.println("Limpiando tamaños N...");
        tamañosN.clear();
    }

    public int getSizeTiemposCercanosON2() {
        return tiemposCercanosON2.size();
    }

    public int getSizeTiemposCercanosONLogN() {
        return tiemposCercanosONLogN.size();
    }

    public int getSizeTiemposLejanos() {
        return tiemposLejanos.size();
    }

    public int getSizeTamañosN() {
        return tamañosN.size();
    }

    public ArrayList<Long> getTiemposCercanosON2() {
        return tiemposCercanosON2;
    }

    public ArrayList<Long> getTiemposCercanosONLogN() {
        return tiemposCercanosONLogN;
    }

    public ArrayList<Long> getTiemposLejanos() {
        return tiemposLejanos;
    }

    public long getTiempoCercanosON2(int i) {
        return tiemposCercanosON2.get(i);
    }

    public long getTiempoCercanosONLogN(int i) {
        return tiemposCercanosONLogN.get(i);
    }

    public long getTiempoLejanos(int i) {
        return tiemposLejanos.get(i);
    }

    public ArrayList<Integer> getTamañosN() {
        return tamañosN;
    }

    public int getTamañoN(int index) {
        return tamañosN.get(index);
    }

    public void addTiempoCercanosON2(long t) {
        tiemposCercanosON2.add(t);
    }

    public void addTiempoCercanosONLogN(long t) {
        tiemposCercanosONLogN.add(t);
    }

    public void addTiempoLejanos(long t) {
        tiemposLejanos.add(t);
    }

    public void addTamañoN(int n) {
        tamañosN.add(n);
    }

    public void addPuntosParaN(ArrayList<Point2D> puntos) {
        puntosPorN.add(puntos);
    }

    public ArrayList<Point2D> getPuntosParaN(int index) {
        if (index >= 0 && index < puntosPorN.size()) {
            return puntosPorN.get(index);
        } else {
            System.err.println("Índice inválido para puntosPorN: " + index);
            return new ArrayList<>(); // Retorna una lista vacía como fallback
        }
    }

    public class Point2D {
        private int x, y;

        public Point2D(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double distanceTo(Point2D other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
