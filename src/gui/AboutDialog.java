package gui;

import util.ImageUtil;
import javax.swing.*;
import java.io.IOException;

/**
 * Klasse des Überfensters *gnihihi*
 */
public class AboutDialog extends JDialog {
    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        setTitle("Über Mathbase");
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        JPanel panel = new JPanel();
        setContentPane(panel);
        try {
            setIconImage(ImageUtil.getImage("images/icon.png"));
            panel.add(new JLabel(ImageUtil.getIcon("images/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.add(new JLabel("Hier stehen die Mitwirkenden, Sponsoren, Quellen etc."));
        setVisible(true);
    }
}
