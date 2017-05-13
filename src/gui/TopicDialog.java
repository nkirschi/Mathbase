package gui;

import util.ElementDataHandler;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Eingabemaske beim Erstellen eines neuen Themas
 * TODO Bene will Map<Pfad, Typ> zurück haben für alle medialen Inhalte und Description selbst TXT und dann zurück
 */
public class TopicDialog extends JDialog {
    private MainFrame mainFrame;

    public TopicDialog(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        setTitle("Thema hinzufügen");
        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        try {
            setIconImage(ImageUtil.getImage("images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initFormPanel();
        initButtonPanel();
        setVisible(true);
    }

    /**
     * Hilfsmethode für die Initialisierung der Schaltflächen
     */
    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Computer sagt: Nein!", "Weltuntergang", JOptionPane.WARNING_MESSAGE));
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Hilfsmethode für die Initialisierung der Maske selbst
     */
    private void initFormPanel() {
        JPanel formPanel = new JPanel();

        JTextField titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(200, 20));
        JLabel titleLabel = new JLabel("Titel des Themas");
        titleLabel.setLabelFor(titleField);
        formPanel.add(titleLabel);
        formPanel.add(titleField);

        getContentPane().add(formPanel, BorderLayout.CENTER);
    }
}
