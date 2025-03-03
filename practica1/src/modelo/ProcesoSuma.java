package modelo;

import control.*;
import java.util.Random;

/**
 * Clase ProcesoSuma que ejecuta la suma de matrices en un hilo separado.
 * 
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */
public class ProcesoSuma extends Thread implements Notificar {

    private volatile boolean cancel;
    private Controlador contr;

    public ProcesoSuma(Controlador c) {
        contr = c;
    }

    @Override
    public void run() {        
        contr.getDatos().clearTiemposSuma();

        // Obtenemos el tamaño de las matrices (son cuadradas)
        Datos dat = contr.getDatos();
        int tamArrayN = dat.getSizeElements(); // Tamaño del array que contiene los distintos n (que representan el
                                               // tamaño de las matrices)
        int tamMatriz; // El valor de la n de cada iteración (tamaño de la matriz)
        int[][] matr1, matr2;
        Random rand = new Random();

        // Iteramos para cada n del array de tamaños de matrices
        for (int idxTamMatriz = 0; idxTamMatriz < tamArrayN && !cancel; idxTamMatriz++) {
            // Obtenemos la n de la iteración actual
            tamMatriz = dat.getElement(idxTamMatriz);
            // Creamos las 2 matrices a multiplicar
            matr1 = new int[tamMatriz][tamMatriz];
            matr2 = new int[tamMatriz][tamMatriz];

            // Las instanciamos con valores aleatorios
            for (int idxFil = 0; idxFil < tamMatriz && !cancel; idxFil++) {
                for (int idxCol = 0; idxCol < tamMatriz && !cancel; idxCol++) {
                    matr1[idxFil][idxCol] = rand.nextInt(0, 9);
                    matr2[idxFil][idxCol] = rand.nextInt(0, 9);
                }
            }

            // Llamamos a la suma de matrices con las matrices inicializadas
            //long tiempo = Algoritmos.sumaMatrices(tamMatriz, matr1, matr2);
            long tiempo = sumaMatrices(tamMatriz, matr1, matr2);

            if(!cancel){

                dat.addTiemposSuma(tiempo);

                // Notificar después de cada cálculo
                contr.notificar("Pintar");

                System.out.println("\nSuma de matrices " + tamMatriz + "x" + tamMatriz +
                        "\n Tiempo(ns) : " + tiempo +
                        "\n Constante Multiplicativa : " + (double)((double)tiempo / ((long)tamMatriz * (long)tamMatriz))); //tamMatriz^2 porque O(n^2)
            }
        }
    }

    private void detener(){
        cancel = true;
    }

    @Override
    public synchronized void notificar(String s) {
        if (s.contentEquals("Detener")) {
            detener();
        }
    }


    // Algoritmo para sumar las matrices con coste asintótico O(n^2)
    private long sumaMatrices(int tamMatriz, int[][] matr1, int[][] matr2) {

        int[][] resultado = new int[tamMatriz][tamMatriz];

        long start = System.nanoTime();
        for (int idxFil = 0; idxFil < tamMatriz && !cancel; idxFil++) {
            for (int idxCol = 0; idxCol < tamMatriz && !cancel; idxCol++) {
                resultado[idxFil][idxCol] = matr1[idxFil][idxCol] + matr2[idxFil][idxCol];
            }
        }
        return System.nanoTime() - start;
    }
}