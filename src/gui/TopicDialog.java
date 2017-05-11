package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Eingabemaske beim Erstellen eines neuen Themas
 */
public class TopicDialog extends JDialog {
    public TopicDialog(MainFrame mainFrame) {
        super(mainFrame);
        setTitle("Thema hinzufügen");
        setSize(700, 500);
        setLocationRelativeTo(mainFrame);
        setVisible(true);
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
