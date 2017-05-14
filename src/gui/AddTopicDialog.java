package gui;

import util.ImageUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;

import util.ElementDataHandler;

/**
 * Eingabemaske beim Erstellen eines neuen Themas
 * TODO Bene will Map<Pfad, Typ> zurück haben für alle medialen Inhalte und Description selbst TXT und dann zurück
 */
public class AddTopicDialog extends JDialog {
    private MainFrame mainFrame;
    private JTextField titleField;
    private JFileChooser fileChooser;

    public AddTopicDialog(MainFrame mainFrame) {
        super(mainFrame);
        this.setModal(true);
        this.mainFrame = mainFrame;
        setTitle("Thema hinzufügen");
        setSize(350, 220);
        setResizable(false);
        setLocationRelativeTo(mainFrame);
        try {
            setIconImage(ImageUtil.getInternalImage("images/icon.png"));
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
        cancelButton.setPreferredSize(new Dimension(85, 24));
        cancelButton.addActionListener(e -> dispose());
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(85, 24));
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener( e -> {
            String name = titleField.getText();
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
        buttonPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Hilfsmethode für die Initialisierung der Maske selbst
     */
    private void initFormPanel() {
        SpringLayout layout = new SpringLayout();
        JPanel formPanel = new JPanel(layout);
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(200, 20));
        JLabel titleLabel = new JLabel("Titel des Themas:");
        titleLabel.setLabelFor(titleField);
        layout.putConstraint(SpringLayout.WEST, titleLabel, 20, SpringLayout.WEST, formPanel);
        layout.putConstraint(SpringLayout.NORTH, titleLabel, 20, SpringLayout.NORTH, formPanel);
        layout.putConstraint(SpringLayout.WEST, titleField, 20, SpringLayout.EAST, titleLabel);
        layout.putConstraint(SpringLayout.NORTH, titleField, 16, SpringLayout.NORTH, formPanel);
        formPanel.add(titleLabel);
        formPanel.add(titleField);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Bilder (*.jpg, *.png)", "jpg", "png"));
        JButton chooseIconButton = new JButton("Icon auswählen...");
        JLabel iconLabel = new JLabel();
        try {
            iconLabel.setIcon(ImageUtil.getInternalIcon("images/witcher.png", 64, 64));
        } catch (IOException e) {
            e.printStackTrace();
        }
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, iconLabel, 0, SpringLayout.HORIZONTAL_CENTER, titleLabel);
        layout.putConstraint(SpringLayout.NORTH, iconLabel, 20, SpringLayout.SOUTH, titleLabel);
        layout.putConstraint(SpringLayout.WEST, chooseIconButton, 0, SpringLayout.WEST, titleField);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, chooseIconButton, 0, SpringLayout.VERTICAL_CENTER, iconLabel);
        iconLabel.setSize(64, 64);
        chooseIconButton.addActionListener(event -> {
            int result = fileChooser.showOpenDialog(AddTopicDialog.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    iconLabel.setIcon(ImageUtil.getExternalIcon(fileChooser.getSelectedFile().getAbsolutePath(), 64, 64));
                    repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        formPanel.add(chooseIconButton);
        formPanel.add(iconLabel);
        getContentPane().add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Hilfsmethode für die Initialisierung des Icon-Auswhlfeldes TODO implementieren
     */
    private void initIconChooser(){

    }
}