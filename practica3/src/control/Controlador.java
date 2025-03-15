package control;

import java.util.ArrayList;
import javax.swing.JFrame;
import modelo.*;
import vista.PanelPrincipal;

/**
 * Controlador principal que implementa el patrón MVC y gestiona la comunicación
 * entre la vista y el modelo.
 *
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
 */
public class Controlador implements Notificar {

    // Panel principal de la interfaz gráfica
    private PanelPrincipal panelPrincipal;
    // Lista de procesos que implementan la interfaz Notificar
    private ArrayList<Notificar> procesos;
    // Objeto que almacena los datos para los cálculos
    private Datos dat;

    /**
     * Punto de entrada principal de la aplicación
     */
    public static void main(String[] args) {
        (new Controlador()).inicio();
    }

    /**
     * Inicializa la aplicación, creando la interfaz gráfica y preparando los
     * datos iniciales
     */
    private void inicio() {
        // Inicialización de las estructuras de datos
        dat = new Datos();
        procesos = new ArrayList<>();
        preparar();

        // Configuración de la ventana principal
        JFrame frame = new JFrame("Puntos 2D - Distancias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelPrincipal = new PanelPrincipal(this);
        frame.setContentPane(panelPrincipal);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        frame.setVisible(true);
    }

    /**
     * Prepara los datos iniciales para los cálculos Genera una secuencia
     * exponencial de tamaños de matrices
     */
    private void preparar() {
        procesos.clear();;
        dat.clear();
        // Generamos tamaños de matrices en progresión lineal: 500, 1000, 1500 y 2000
        for (int i = 500; i <= 2000; i += 500) {
            dat.addTamañoN(i);
        }
    }

    /**
     * Implementación del método notificar de la interfaz Notificar Gestiona las
     * diferentes acciones del programa de forma sincronizada
     *
     * @param s String que indica la acción a realizar ("arrancar", "detener",
     * "pintar")
     */
    @Override
    public synchronized void notificar(String s) {
        switch (s) {
            case "CercanosON2" -> {
                //Booleano para determinar si se ha encontrado un Proceso Producto en algún momento (ya sea vivo o muerto)
                boolean encontrado = false;
                //Si existen procesos en la lista, los recorremos
                if (!procesos.isEmpty()) {
                    // Recorremos cada proceso que exista
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        //Si está vivo...
                        if (((Thread) procesos.get(idx)).isAlive()) {
                            // ... y es un proceso producto, no se puede ejecutar otro
                            if (procesos.get(idx) instanceof ProcesoCercanosON2) {  //Si el nombre de la clase del proceso que miramos es "ProcesoCercanoON2"
                                System.err.println("Ya hay un proceso O(n^2) en ejecución");
                                encontrado = true;
                                break; //Salimos del bucle porque solo hay 1 proceso de cada tipo
                            }
                        // Si este hilo muerto es el Proceso Producto -> lo arrancamos
                        } else if (procesos.get(idx) instanceof ProcesoCercanosON2) { 
                                //Reiniciamos el array de tiempos del producto
                                dat.clearTiemposCercanosON2();
                                // Lo creamos y arrancamos                                
                                procesos.set(idx, new ProcesoCercanosON2(this));
                                ((Thread) procesos.get(idx)).start();
                                encontrado = true;
                                break; //Ya hemos arrancado el proceso -> podemos salir ya del bucle
                            }
                        }
                    }
                //Si no hay ningún proceso creado aún o no se ha encontrado ningún proceso suma...
                if (procesos.isEmpty() || !encontrado) {
                    //... entonces simplemente creamos un proceso suma por primera vez
                    procesos.add(new ProcesoCercanosON2(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();  //procesos.size()-1 porque lo acabamos de meter al final
                }
            }
            case "CercanosONLogN" -> {
                //Booleano para determinar si se ha encontrado un Proceso Producto en algún momento (ya sea vivo o muerto)
                boolean encontrado = false;
                //Si existen procesos en la lista, los recorremos
                if (!procesos.isEmpty()) {
                    // Recorremos cada proceso que exista
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        //Si está vivo...
                        if (((Thread) procesos.get(idx)).isAlive()) {
                            // ... y es un proceso producto, no se puede ejecutar otro
                            if (procesos.get(idx) instanceof ProcesoCercanosONLogN) {  //Si el nombre de la clase del proceso que miramos es "ProcesoCercanoON2"
                                System.err.println("Ya hay un proceso O(n·log n) en ejecución");
                                encontrado = true;
                                break; //Salimos del bucle porque solo hay 1 proceso de cada tipo
                            }
                        // Si este hilo muerto es el Proceso Producto -> lo arrancamos
                        } else if (procesos.get(idx) instanceof ProcesoCercanosONLogN) { 
                                //Reiniciamos el array de tiempos del producto
                                dat.clearTiemposCercanosONLogN();
                                // Lo creamos y arrancamos                                
                                procesos.set(idx, new ProcesoCercanosONLogN(this));
                                ((Thread) procesos.get(idx)).start();
                                encontrado = true;
                                break; //Ya hemos arrancado el proceso -> podemos salir ya del bucle
                            }
                        }
                    }
                //Si no hay ningún proceso creado aún o no se ha encontrado ningún proceso suma...
                if (procesos.isEmpty() || !encontrado) {
                    //... entonces simplemente creamos un proceso suma por primera vez
                    procesos.add(new ProcesoCercanosONLogN(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();  //procesos.size()-1 porque lo acabamos de meter al final
                }
            }
            case "Lejanos" -> {
                boolean encontrado = false;
                if (!procesos.isEmpty()) {
                    for (int i = 0; i < procesos.size(); i++) {
                        if (((Thread) procesos.get(i)).isAlive()) {
                            if (procesos.get(i) instanceof ProcesoLejanos) {
                                System.err.println("Ya hay un proceso de lejanos en ejecución");
                                encontrado = true;
                                break;
                            }
                        } else if (procesos.get(i) instanceof ProcesoLejanos) {
                            dat.clearTiemposLejanos();
                            procesos.set(i, new ProcesoLejanos(this));
                            ((Thread) procesos.get(i)).start();
                            encontrado = true;
                            break;
                        }
                    }
                }
                if (procesos.isEmpty() || !encontrado) {
                    procesos.add(new ProcesoLejanos(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();
                }
            }
            case "Detener" -> {
                // Detiene todos los procesos en ejecución
                for (int i = 0; i < procesos.size(); i++) {
                    if (((Thread) procesos.get(i)).isAlive()) {
                        procesos.get(i).notificar("Detener");
                    }
                }
            }
            case "Pintar" -> {
                // Solicita al panel principal que se actualice
                panelPrincipal.notificar("Pintar");
            }
            default -> {
                // No hace nada para otros casos
            }
        }
    }

    /**
     * Devuelve el objeto Datos que contiene la información de los cálculos
     *
     * @return Objeto Datos con la información de los cálculos
     */
    public Datos getDatos() {
        return dat;
    }
}
