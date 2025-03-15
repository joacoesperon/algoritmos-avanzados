package modelo;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Datos {

    private ArrayList<Point2D> puntos; // Lista de puntos 2D
    private ArrayList<Long> tiemposCercanosON2; // Tiempos para O(n²)
    private ArrayList<Long> tiemposCercanosONLogN; // Tiempos para O(n·log n)
    private ArrayList<Long> tiemposLejanos; // Tiempos para la pareja más lejana
    private ArrayList<Integer> tamañosN; // Tamaños de los conjuntos de puntos

    public Datos() {
        puntos = new ArrayList<>();
        tiemposCercanosON2 = new ArrayList<>();
        tiemposCercanosONLogN = new ArrayList<>();
        tiemposLejanos = new ArrayList<>();
        tamañosN = new ArrayList<>();
    }
    
    public void clear() {
        puntos.clear();
        tiemposCercanosON2.clear();
        tiemposCercanosONLogN.clear();
        tiemposLejanos.clear();
        tamañosN.clear();
    }

    public void clearTiemposCercanosON2() {
        tiemposCercanosON2.clear(); 
    }

    public void clearTiemposCercanosONLogN() { 
        tiemposCercanosONLogN.clear(); 
    }

    public void clearTiemposLejanos() { 
        tiemposLejanos.clear(); 
    }

    public void clearTamañosN() { 
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

    public int getTamañoN(int i) { 
        return tamañosN.get(i); 
    }
    
    public void addPunto(Point2D p) { 
        puntos.add(p); 
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

    public void generarPuntosAleatorios(int n, int rango) {
        Random rand = new Random();
        puntos.clear();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(rango);
            int y = rand.nextInt(rango);
            puntos.add(new Point2D(x, y));
        }
    }

    public ArrayList<Point2D> getPuntos() {
        return puntos;
    }

    public Point2D getPunto(int i) { 
        return puntos.get(i); 
    }

    public int getNumPuntos() { 
        return puntos.size(); 
    }

    // Clase auxiliar para representar puntos 2D
    class Point2D {
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
    }
}
