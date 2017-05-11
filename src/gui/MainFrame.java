package gui;
import util.ImageUtil;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame implements WindowListener {
    private AbstractView currentView;

    private MainFrame() {
        setTitle("Mathbase Alpha 1.1.2_01");
        setSize(800, 600);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        try {
            setIconImage(ImageUtil.getImage("images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initMenuBar();
        changeTo(new MenuView(this));
        setVisible(true);
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
     * Hilfsmethode zur Initialisierung der Menüleiste
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> System.exit(0));
        try {
            exitItem.setIcon(new ImageIcon(ImageUtil.getImage("images/exit.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileMenu.add(exitItem);
        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem aboutItem = new JMenuItem("Über");
        //aboutItem.addActionListener(); Hier würde dann Impressum und sowas kommen
        try {
            aboutItem.setIcon(new ImageIcon(ImageUtil.getImage("images/help.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Getter-Methode für die aktuelle Ansicht
     * @return Momentan aktive Ansicht
     */
    public AbstractView getCurrentView() {
        return currentView;
    }

    /**
     * Diese Methode wird von Swing beim Versuch des Schließens ausgeführt
     * @param e Das WindowEvent mit Detailinformationen (brauchen wir hier aber nicht)
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(this, "Wirklich beenden?", "Mathbase", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            System.exit(0);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}