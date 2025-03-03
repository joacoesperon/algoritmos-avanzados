package modelo;

import java.util.ArrayList;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Datos {

    private ArrayList<Long> tiemposSuma;
    private ArrayList<Long> tiemposProducto;
    private ArrayList<Integer> elementosN;

    public Datos() {
        tiemposSuma = new ArrayList<>();
        tiemposProducto = new ArrayList<>();
        elementosN = new ArrayList<>();
    }
    
    public void clear() {
        tiemposSuma.clear();
        tiemposProducto.clear();
        elementosN.clear();
    }

    public void clearTiemposSuma() {
        tiemposSuma.clear();
    }

    public void clearTiemposProducto() {
        tiemposProducto.clear();
    }
    
    public void clearElementosN() {
        elementosN.clear();
    }
    
    public int getSizeTiemposSuma() {
        return tiemposSuma.size();
    }
    
    public int getSizeTiemposProducto() {
        return tiemposProducto.size();
    }
    
    public int getSizeElements() {
        return elementosN.size();
    }
    
    public long getTiemposSuma(int i) {
        return(tiemposSuma.get(i));
    }
    
    public long getTiemposProducto(int i) {
        return(tiemposProducto.get(i));
    }
    
    public int getElement(int i) {
        return elementosN.get(i);
    }
    
    public synchronized void addTiemposSuma(long t) {
        tiemposSuma.add(t);
    }
    
    public synchronized void addTiemposProducto(long t) {
        tiemposProducto.add(t);
    }
    
    public synchronized void addElement(int n) {
        elementosN.add(n);
    }
}
