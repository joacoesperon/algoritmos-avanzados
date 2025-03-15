package modelo;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Datos {
    private int tamano; // Tamaño del tablero (2, 4, 8, 16, 32, 64)
    private int[][] tablero; // Matriz que representa el tablero
    private int libreFila, libreColumna; // Posición del cuadro libre

    public Datos(){
        this.tamano = 0;
        this.tablero = new int[tamano][tamano];
        this.libreFila = 0;
        this.libreColumna = 0;
    }

    public int getTamano() { 
        return tamano; 
    }
    public int[][] getTablero() { 
        return tablero; 
    }
    public int getLibreX() { 
        return libreFila; 
    }
    public int getLibreY() { 
        return libreColumna; 
    }

    //no se usa, se implementa dentro de reiniciarDatos
    public void setLibre() {
        if (libreFila >= 0 && libreFila < tamano && libreColumna >= 0 && libreColumna < tamano) {
            this.tablero[libreFila][libreColumna] = -1;
        }else{
            System.err.println("Error: las coordenadas del bloque libre exceden los limites del tablero.");
        }
    }

    public void reiniciarDatos(int tamano, int fila, int columna) {
        this.tamano = tamano;
        this.tablero = new int[tamano][tamano];
        //manejo del bloque libre
        this.libreFila = fila;
        this.libreColumna = columna;
        setLibre();
        //imprimirDatos();
    }
    
    public void setBloque(int x, int y,int valor){
        this.tablero[x][y] = valor;
    }
    
    // Método para encontrar el valor máximo (número del tromino más alto)
    public int obtenerMaxTromino() {
        int max = -1;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] > max) {
                    max = tablero[i][j];
                }
            }
        }
        return max;
    }
    
    // Método para encontrar las posiciones de un tromino específico
    public int[][] encontrarTromino(int tromino) {
        java.util.List<int[]> posiciones = new java.util.ArrayList<>();
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] == tromino) {
                    posiciones.add(new int[]{i, j});
                }
            }
        }
        return posiciones.toArray(new int[0][]);
    }
    
    public void imprimirDatos(){
        System.out.println("tamano: "+ tamano);
        System.out.println("libreX: "+ libreFila);
        System.out.println("libreY: "+ libreColumna);
    }
    
    // Método de depuración para imprimir el tablero
    public void imprimirTablero() {
        System.out.println("Estado del tablero:");
        for (int i = 0; i < this.tamano; i++) {
            for (int j = 0; j < this.tamano; j++) {
                System.out.print(this.tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
}