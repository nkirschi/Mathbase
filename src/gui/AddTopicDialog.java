package gui;

import util.ElementDataHandler;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

import static java.nio.file.Files.copy;
import static util.Logger.*;

/**
 * Eingabemaske beim Erstellen eines neuen Themas
 * TODO Bene will Map<Pfad, Typ> zurück haben für alle medialen Inhalte und Description selbst TXT und dann zurück
 */
public class AddTopicDialog extends JDialog {
    private MainFrame mainFrame;
    private JTextField titleField;
    private JFileChooser fileChooser;
    private File icon;

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
        cancelButton.setPreferredSize(new Dimension(88, 24));
        cancelButton.addActionListener(a -> dispose());
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(88, 24));
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(a -> {
            String name = titleField.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen Titel an!", "Achtung", JOptionPane.WARNING_MESSAGE);
            } else {
                String key = String.valueOf(System.nanoTime());
                String iconpath = "";
                File dir = new File("topics/" + key);
                if (dir.mkdirs()) {
                    log(INFO, "Ordner erfolgreich erstellt!");
                    if (icon != null) {
                        try {
                            iconpath = copy(icon.toPath(), new File("topics/" + key + "/" + icon.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING).toString();
                        } catch (IOException e1) {
                            log(WARNING, e1);
                            e1.printStackTrace();
                        }
                    }
                    ElementDataHandler handler = ElementDataHandler.getElementDataHandler();
                    handler.addTheme(key, name, iconpath);
                    handler.safeElementData();
                    mainFrame.getCurrentView().update();
                    dispose();
                } else {
                    log(WARNING, "Thema nicht erstellt; konnte Ordner nicht erstellen!");
                    dispose();
                }
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
        titleField.setPreferredSize(new Dimension(250, 20));
        JLabel titleLabel = new JLabel("Titel:");
        titleLabel.setLabelFor(titleField);

        formPanel.add(titleLabel);
        formPanel.add(titleField);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Bilder (*.jpg, *.png)", "jpg", "png", "gif"));
        JButton chooseIconButton = new JButton("Icon auswählen...");
        JLabel iconLabel = new JLabel();
        iconLabel.setSize(64, 64);
        try {
            iconLabel.setIcon(ImageUtil.getInternalIcon("images/icon.png", 64, 64));
        } catch (IOException e) {
            e.printStackTrace();
        }

        layout.putConstraint(SpringLayout.WEST, titleLabel, 30, SpringLayout.WEST, formPanel);
        layout.putConstraint(SpringLayout.NORTH, titleLabel, 20, SpringLayout.NORTH, formPanel);
        layout.putConstraint(SpringLayout.WEST, titleField, 10, SpringLayout.EAST, titleLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, titleField, 0, SpringLayout.VERTICAL_CENTER, titleLabel);

        layout.putConstraint(SpringLayout.WEST, iconLabel, 30, SpringLayout.WEST, formPanel);
        layout.putConstraint(SpringLayout.NORTH, iconLabel, 25, SpringLayout.SOUTH, titleField);
        layout.putConstraint(SpringLayout.WEST, chooseIconButton, 20, SpringLayout.EAST, iconLabel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, chooseIconButton, 0, SpringLayout.VERTICAL_CENTER, iconLabel);

        chooseIconButton.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(AddTopicDialog.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                icon = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    iconLabel.setIcon(ImageUtil.getExternalIcon(icon.getAbsolutePath(), 64, 64));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        formPanel.add(chooseIconButton);
        formPanel.add(iconLabel);
        getContentPane().add(formPanel, BorderLayout.CENTER);
    }
}