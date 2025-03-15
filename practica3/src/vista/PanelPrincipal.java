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
        
        JMenuItem cercanosON2 = new JMenuItem("Cercanos  O(n^2)");
        JMenuItem cercanosONLogN = new JMenuItem("Cercanos  O(n*logn)");
        JMenuItem lejanos = new JMenuItem("Lejanos");
        JMenuItem detener = new JMenuItem("Detener");
        
        procMenu.add(cercanosON2);
        procMenu.addSeparator();
        procMenu.add(cercanosONLogN);
        procMenu.addSeparator();
        procMenu.add(lejanos);
        procMenu.addSeparator();
        procMenu.add(detener);
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
        cercanosON2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("CercanosON2");
            }
        });

        cercanosONLogN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("CercanosONLogN");
            }
        });

        lejanos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contr.notificar("Lejanos");
            }
        });
        
        detener.addActionListener(new ActionListener() {
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
