package vista;

import control.Controlador;
import control.Notificar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Joaquin Esperon Solari
 * @author Marc Nadal Sastre Gondar
*/
public class PanelPrincipal extends JPanel implements Notificar {
    
    private Controlador contr;
    private Grafica grafica;

    public PanelPrincipal(Controlador c) {
        contr = c;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        
        JMenu procMenu = new JMenu("Procesos");
        
        JMenuItem arrancarSumaItem = new JMenuItem("Arrancar Suma");
        JMenuItem arrancarProductoItem = new JMenuItem("Arrancar Producto");
        JMenuItem arrancarAmbosItem = new JMenuItem("Arrancar Ambos");
        JMenuItem aturarItem = new JMenuItem("Detener");
        
        procMenu.add(arrancarSumaItem);
        procMenu.addSeparator();

        procMenu.add(arrancarProductoItem);
        procMenu.addSeparator();

        procMenu.add(arrancarAmbosItem);
        procMenu.addSeparator();

        procMenu.add(aturarItem);
        
        menuBar.add(procMenu);
        
        // Crear el panel principal con coordenadas absolutas
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // Permite la colocación de elementos en posiciones absolutas
        mainPanel.setBounds(0, 0, 800, 600);
        grafica = new Grafica(800, 600, contr);
        mainPanel.add(grafica);
        
        // Agregar el menú en la parte superior
        add(menuBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Agregar listeners a los elementos del menú
        arrancarSumaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("Arrancar Suma");
            }
        });

        arrancarProductoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("Arrancar Producto");
            }
        });

        arrancarAmbosItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("Arrancar Ambos");
            }
        });
        
        aturarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("Detener");
            }
        });
    }
    
    @Override
    public void notificar(String s) {
        if (s.startsWith("Pintar")) {
            grafica.pintar();
        }
    }
}
