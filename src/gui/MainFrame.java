package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame {
    private AbstractView currentView;

    public MainFrame() {
        setTitle("Mathbase Alpha 1.1.2_01");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Wirklich beenden?", "Mathbase", 0);
                if (result == 0)
                    System.exit(0);
            }
        });
        setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
        setVisible(true);
        setResizable(false);
    }

    /**
     * Die Hauptmethode der Applikation
     * @param args Irrelevante Kommandozeilenparamter
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();
    }

    /**
     * Eine wichtige Methode, um die aktuelle Ansicht zu wechseln
     * @param view Ansicht, zu der gewechselt werden soll
     */
    public void changeTo(AbstractView view) {
        if(currentView != null){
            remove(currentView);
        }
        currentView = view;
        add(currentView);
        revalidate();
        repaint();
        currentView.revalidate();
        currentView.update();
        currentView.repaint();
    }

    /**
     * Getter-Methode für die aktuelle Ansicht
     * @return Momentan aktive Ansicht
     */
    public AbstractView getCurrentView() {
        return currentView;
    }
}