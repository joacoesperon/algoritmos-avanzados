package modelo;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class Datos {
    private int tamano; // Tamaño del tablero (2, 4, 8, 16, 32, 64)
    private int[][] tablero; // Matriz que representa el tablero
    private int libreX, libreY; // Posición del cuadro libre

    public Datos(int tamano) {
        this.tamano = tamano;
        this.tablero = new int[tamano][tamano];
        this.libreX = 0;
        this.libreY = 0;
    }

    public int getTamano() { return tamano; }
    public int[][] getTablero() { return tablero; }
    public int getLibreX() { return libreX; }
    public int getLibreY() { return libreY; }

    public void setLibre(int x, int y) {
        this.libreX = x;
        this.libreY = y;
    }

    public void reiniciarTablero() {
        this.tablero = new int[tamano][tamano];
    }
}
