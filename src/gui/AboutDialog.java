package gui;

import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Klasse des Überfensters *gnihihi*
 */
public class AboutDialog extends JDialog {
    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        setTitle("Thema hinzufügen");
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        JPanel panel = new JPanel();
        setContentPane(panel);
        try {
            panel.add(new JLabel(new ImageIcon(ImageUtil.getImage("images/icon.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.add(new JLabel("Hier stehen die Mitwirkenden, Sponsoren, Quellen etc."));
        setVisible(true);
    }
}
