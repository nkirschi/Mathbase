import javax.swing.*;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame {
    private AbstractView currentView;

    public MainFrame() {
        setTitle("PSem Alpha 1.1.2_01");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Die Hauptmethode der Applikation
     * @param args Irrelevante Kommandozeilenparamter
     */
    public static void main(String[] args) {
        new MainFrame();
    }
}

//Töööööööööööööst