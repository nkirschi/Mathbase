package gui;

import util.ImageUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import util.ElementDataHandler;

/**
 * Eingabemaske beim Erstellen eines neuen Themas
 * TODO Bene will Map<Pfad, Typ> zurück haben für alle medialen Inhalte und Description selbst TXT und dann zurück
 */
public class TopicDialog extends JDialog {
    private MainFrame mainFrame;
    private JTextField textField;

    public TopicDialog(MainFrame mainFrame) {
        super(mainFrame);
        this.setModal(true);
        this.mainFrame = mainFrame;
        setTitle("Thema hinzufügen");
        setSize(250, 200);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        try {
            setIconImage(ImageUtil.getImage("images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        textField=initFormPanel();
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
        okButton.addActionListener( e -> {
            String name=textField.getText();
            if (name.compareTo("")==0){
                JOptionPane.showMessageDialog(this, "Geben Sie einen Titel ein!", "", JOptionPane.WARNING_MESSAGE);
            }
            else{
                ElementDataHandler handler=ElementDataHandler.getElementDataHandler();
                handler.addTheme(name); //TODO icon hinzufügen
                handler.safeElementData();
                mainFrame.getCurrentView().update();
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Hilfsmethode für die Initialisierung der Maske selbst
     */
    private JTextField initFormPanel() {
        JPanel formPanel = new JPanel();
        JTextField titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(200, 20));
        JLabel titleLabel = new JLabel("Titel des Themas:");
        titleLabel.setLabelFor(titleField);
        formPanel.add(titleLabel);
        formPanel.add(titleField);
        getContentPane().add(formPanel, BorderLayout.CENTER);
        return titleField;
    }

    /**
     * Hilfsmethode für die Initialisierung des Icon-Auswhlfeldes TODO implementieren
     */
    private void initIconChooser(){

    }
}