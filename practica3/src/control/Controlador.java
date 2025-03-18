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
        procesos.clear();
        dat.clear();
        dat.preparar();
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
            case "CercanosON2":
                boolean encontrado = false;
                if (!procesos.isEmpty()) {
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        if (((Thread) procesos.get(idx)).isAlive()) {
                            if (procesos.get(idx) instanceof ProcesoCercanosON2) {
                                System.err.println("Ya hay un proceso O(n^2) en ejecución");
                                encontrado = true;
                                break;
                            }
                        } else if (procesos.get(idx) instanceof ProcesoCercanosON2) {
                            //System.out.println("Reiniciando proceso O(n^2) en índice " + idx);
                            dat.clearTiemposCercanosON2();
                            procesos.set(idx, new ProcesoCercanosON2(this));
                            ((Thread) procesos.get(idx)).start();
                            encontrado = true;
                            break;
                        }
                    }
                }
                if (procesos.isEmpty() || !encontrado) {
                    //System.out.println("Creando nuevo proceso O(n^2)");
                    procesos.add(new ProcesoCercanosON2(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();
                }
                break;
            case "CercanosONLogN":
                encontrado = false;
                if (!procesos.isEmpty()) {
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        if (((Thread) procesos.get(idx)).isAlive()) {
                            if (procesos.get(idx) instanceof ProcesoCercanosONLogN) {
                                System.err.println("Ya hay un proceso O(n·log n) en ejecución");
                                encontrado = true;
                                break;
                            }
                        } else if (procesos.get(idx) instanceof ProcesoCercanosONLogN) {
                            //System.out.println("Reiniciando proceso O(n·log n) en índice " + idx);
                            dat.clearTiemposCercanosONLogN();
                            procesos.set(idx, new ProcesoCercanosONLogN(this));
                            ((Thread) procesos.get(idx)).start();
                            encontrado = true;
                            break;
                        }
                    }
                }
                if (procesos.isEmpty() || !encontrado) {
                    //System.out.println("Creando nuevo proceso O(n·log n)");
                    procesos.add(new ProcesoCercanosONLogN(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();
                }
                break;
            case "Lejanos":
                encontrado = false;
                if (!procesos.isEmpty()) {
                    for (int i = 0; i < procesos.size(); i++) {
                        if (((Thread) procesos.get(i)).isAlive()) {
                            if (procesos.get(i) instanceof ProcesoLejanos) {
                                System.err.println("Ya hay un proceso de lejanos en ejecución");
                                encontrado = true;
                                break;
                            }
                        } else if (procesos.get(i) instanceof ProcesoLejanos) {
                            //System.out.println("Reiniciando proceso de lejanos en índice " + i);
                            dat.clearTiemposLejanos();
                            procesos.set(i, new ProcesoLejanos(this));
                            ((Thread) procesos.get(i)).start();
                            encontrado = true;
                            break;
                        }
                    }
                }
                if (procesos.isEmpty() || !encontrado) {
                    //System.out.println("Creando nuevo proceso de lejanos");
                    procesos.add(new ProcesoLejanos(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();
                }
                break;
            case "Detener":
                System.out.println("Deteniendo todos los procesos...");
                for (int i = 0; i < procesos.size(); i++) {
                    if (((Thread) procesos.get(i)).isAlive()) {
                        procesos.get(i).notificar("Detener");
                    }
                }
                break;
            case "Pintar":
                panelPrincipal.notificar("Pintar");
                break;
            default:
                System.out.println("Notificación no reconocida: " + s);
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
