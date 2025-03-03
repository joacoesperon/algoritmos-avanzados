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
    private ArrayList<Notificar> procesos = null;
    // Objeto que almacena los datos para los cálculos
    private Datos dat = null;

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
        JFrame frame = new JFrame("Aplicación con Menú y Panel Absoluto");
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
        procesos = new ArrayList<>();

        dat.clear();
        // Generamos tamaños de matrices en progresión lineal: 500, 1000, 1500 y 2000
        for (int i = 500; i <= 2000; i += 500) {
            dat.addElement(i);
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
            case "Arrancar Ambos" -> {
                // Verifica si hay procesos en ejecución
                int vivos = 0;
                for (int i = 0; i < procesos.size(); i++) {
                    if (((Thread) procesos.get(i)).isAlive()) {
                        vivos++;
                    }
                }
                // Si no hay procesos activos, inicia nuevos procesos
                if (vivos == 0) {
                    //Reset de los datos y los procesos
                    preparar();

                    // Crea y añade los procesos de suma y producto          
                    procesos.add(new ProcesoSuma(this));
                    procesos.add(new ProcesoProducto(this));

                    // Inicia todos los procesos
                    for (int i = 0; i < procesos.size(); i++) {
                        ((Thread) procesos.get(i)).start();
                    }
                } else {
                    System.err.println("No se puede ejecutar porque ya hay uno o mas proceso/s en ejecucion");
                }
            }
            case "Arrancar Suma" -> {

                //Booleano para determinar si se ha encontrado un proceso suma en algún momento (ya sea vivo o muerto)
                boolean encontrado = false;

                //Si existen procesos en la lista, los recorremos
                if (!procesos.isEmpty()) {

                    // Recorremos cada proceso que exista
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        //Si está vivo...
                        if (((Thread) procesos.get(idx)).isAlive()) {
                            // ... y es un proceso suma, no se puede ejecutar otro

                            if (procesos.get(idx).getClass().getName().equals(modelo.ProcesoSuma.class.getName())) {  //Si el nombre de la clase del proceso que miramos es "ProcesoSuma"
                                System.err.println("No se puede ejecutar porque ya hay un Proceso Suma en ejecucion");
                                encontrado = true;
                                break; //Salimos del bucle porque solo hay 1 proceso de cada tipo
                            }

                        } else { //El hilo está muerto
                            // Si este hilo muerto es el proceso suma -> lo arrancamos
                            if (procesos.get(idx).getClass().getName().equals(modelo.ProcesoSuma.class.getName())) {  //Si el nombre de la clase del proceso que miramos es "ProcesoSuma"
                                //Reiniciamos el array de tiempos de la suma
                                dat.clearTiemposSuma();

                                // Lo creamos y arrancamos                                
                                procesos.set(idx, (new ProcesoSuma(this)));
                                ((Thread) procesos.get(idx)).start();
                                encontrado = true;
                                break; //Ya hemos arrancado el proceso -> podemos salir ya del bucle
                            }
                        }
                    }
                }
                //Si no hay ningún proceso creado aún o no se ha encontrado ningún proceso suma...
                if (procesos.isEmpty() || !encontrado) {

                    //... entonces simplemente creamos un proceso suma por primera vez
                    procesos.add(new ProcesoSuma(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();  //procesos.size()-1 porque lo acabamos de meter al final
                }
            }
            case "Arrancar Producto" -> {

                //Booleano para determinar si se ha encontrado un Proceso Producto en algún momento (ya sea vivo o muerto)
                boolean encontrado = false;

                //Si existen procesos en la lista, los recorremos
                if (!procesos.isEmpty()) {

                    // Recorremos cada proceso que exista
                    for (int idx = 0; idx < procesos.size(); idx++) {
                        //Si está vivo...
                        if (((Thread) procesos.get(idx)).isAlive()) {

                            // ... y es un proceso producto, no se puede ejecutar otro
                            if (procesos.get(idx).getClass().getName().equals(modelo.ProcesoProducto.class.getName())) {  //Si el nombre de la clase del proceso que miramos es "ProcesoProducto"

                                System.err.println("No se puede ejecutar porque ya hay un Proceso Producto en ejecucion");
                                encontrado = true;
                                break; //Salimos del bucle porque solo hay 1 proceso de cada tipo
                            }

                        } else { //El hilo está muerto
                            // Si este hilo muerto es el Proceso Producto -> lo arrancamos
                            if (procesos.get(idx).getClass().getName().equals(modelo.ProcesoProducto.class.getName())) {  //Si el nombre de la clase del proceso que miramos es "ProcesoProducto"
                                //Reiniciamos el array de tiempos del producto
                                dat.clearTiemposProducto();

                                // Lo creamos y arrancamos                                
                                procesos.set(idx, (new ProcesoProducto(this)));
                                ((Thread) procesos.get(idx)).start();
                                encontrado = true;
                                break; //Ya hemos arrancado el proceso -> podemos salir ya del bucle
                            }
                        }
                    }
                }
                //Si no hay ningún proceso creado aún o no se ha encontrado ningún proceso suma...
                if (procesos.isEmpty() || !encontrado) {

                    //... entonces simplemente creamos un proceso suma por primera vez
                    procesos.add(new ProcesoProducto(this));
                    ((Thread) procesos.get(procesos.size() - 1)).start();  //procesos.size()-1 porque lo acabamos de meter al final
                }
            }

            case "Detener" -> {
                // Detiene todos los procesos en ejecución
                for (int i = 0; i < procesos.size(); i++) {
                    if (((Thread) procesos.get(i)).isAlive()) {
                        procesos.get(i).notificar("Detener");
                    }
                }

                //Reiniciamos los valores. Es redundante en un caso, pero útil en los otros. 
                //preparar();
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
