package modelo;

import control.*;
import java.util.Random;

/**
 * Clase ProcesoProducto que ejecuta el producto de matrices en un hilo
 * separado.
 * 
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */

public class ProcesoProducto extends Thread implements Notificar {

    private volatile boolean cancel;
    private final Controlador contr;

    public ProcesoProducto(Controlador c) {
        contr = c;
    }

    @Override
    public void run() {

        contr.getDatos().clearTiemposProducto();
        
        // Obtenemos el tamaño de las matrices (son cuadradas)
        Datos dat = contr.getDatos();
        int tamArrayN = dat.getSizeElements(); // Tamaño del array que contiene los distintos n (que representan el
                                               // tamaño de las matrices)
        int tamMatriz; // El valor de la n de cada iteración (tamaño de la matriz)
        int[][] matr1, matr2;

        Random rand = new Random();

        // Iteramos para cada n del array de tamaños de matrices
        for (int idxTamMatriz = 0; (idxTamMatriz < tamArrayN) && (!cancel); idxTamMatriz++) {

            // Obtenemos la n de la iteración actual
            tamMatriz = dat.getElement(idxTamMatriz);
            // Creamos las 2 matrices a multiplicar
            matr1 = new int[tamMatriz][tamMatriz];
            matr2 = new int[tamMatriz][tamMatriz];

            // Las instanciamos con valores aleatorios
            for (int idxFil = 0; (idxFil < tamMatriz) && (!cancel); idxFil++) {
                for (int idxCol = 0; (idxCol < tamMatriz) && (!cancel); idxCol++) {
                    matr1[idxFil][idxCol] = rand.nextInt(0, 9);
                    matr2[idxFil][idxCol] = rand.nextInt(0, 9);
                }
            }

            // Llamamos a la suma de matrices con las matrices inicializadas
            long tiempo = productoMatrices(tamMatriz, matr1, matr2);

            if (!cancel) {
                dat.addTiemposProducto(tiempo);

                // Notificar después de cada cálculo
                contr.notificar("Pintar");

                System.out.println("\nProducto de matrices " + tamMatriz + "x" + tamMatriz +
                        "\n Tiempo(ns) : " + tiempo +
                        "\n Constante Multiplicativa : " + (double)((double)tiempo / ((long)tamMatriz * (long)tamMatriz * (long)tamMatriz))); //tamMatriz^3 porque el algoritmo es O(n^3)
            }
        }
    }

    //private synchronized void detener() {
    private void detener() {
        cancel = true;
    }

    @Override
    public void notificar(String s) {
        if (s.contentEquals("Detener")) {
            detener();
        }
    }


    // Algoritmos para multiplicar las matrices con coste asintótico O(n^3)
    private long productoMatrices(int tamMatriz, int[][] matr1, int[][] matr2) {

        int[][] resultado = new int[tamMatriz][tamMatriz];

        long start = System.nanoTime();
        for (int idxFil = 0; (idxFil < tamMatriz) && (!cancel); idxFil++) {
            for (int idxCol = 0; (idxCol < tamMatriz) && (!cancel); idxCol++) {
                for (int idxSum = 0; (idxSum < tamMatriz) && (!cancel); idxSum++) {
                    resultado[idxFil][idxCol] += matr1[idxFil][idxSum] * matr2[idxSum][idxCol];
                }
            }
        }
        return System.nanoTime() - start;
    }
}
