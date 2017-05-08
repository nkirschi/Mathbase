import javax.swing.*;
import java.awt.*;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame {
    private AbstractView currentView;

    public MainFrame() {
        setTitle("PSem Alpha 1.1.2_01");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        setVisible(true);
        setResizable(false);
    }

    /**
     * Die Hauptmethode der Applikation
     * @param args Irrelevante Kommandozeilenparamter
     */
    public static void main(String[] args) {
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