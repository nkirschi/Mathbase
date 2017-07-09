/*
 * Copyright (c) 2017 MathBox P-Seminar 16/18. All rights reserved.
 * This product is licensed under the GNU General Public License v3.0.
 * See LICENSE file for further information.
 */

package de.apian.mathbase.gui;

import util.ElementDataHandler;
import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

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
            setIconImage(ImageUtil.getInternalImage("de/apian/mathbase/images/icon.png"));
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
        cancelButton.setMargin(new Insets(3, 12, 3, 12));
        cancelButton.addActionListener(a -> dispose());
        JButton okButton = new JButton("OK");
        okButton.setMargin(new Insets(3, 30, 3, 30));
        okButton.setName("OK");
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(a -> {
            String name = titleField.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen Titel an!", "Achtung", JOptionPane.WARNING_MESSAGE);
            } else {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
                String key = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) +
                        Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND)) +
                        Integer.toString(calendar.get(Calendar.MILLISECOND));
                String iconpath = "";
                File dir = new File("topics/" + key);
                if (dir.mkdirs()) {
                    log(INFO, "Ordner erfolgreich erstellt!");
                    if (icon != null) {
                        try {
                            Image image = ImageUtil.getExternalIcon(icon.getAbsolutePath(), 32, 32).getImage();
                            BufferedImage b = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g = b.createGraphics();
                            g.setComposite(AlphaComposite.Src);
                            g.drawImage(image, 0, 0, null);
                            g.dispose();
                            File file = new File("topics/" + key + "/icon.png");
                            ImageIO.write(b, "png", file);
                            iconpath = file.getPath();
                            //iconpath = copy(icon.toPath(), new File("topics/" + key + "/" + icon.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING).toString();
                        } catch (IOException e1) {
                            log(WARNING, e1);
                            e1.printStackTrace();
                        }
                    }
                    ElementDataHandler handler = ElementDataHandler.getElementDataHandler();
                    handler.addTheme(key, name, iconpath);
                    handler.save();
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
        titleField = new JTextField(30);
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
            iconLabel.setIcon(ImageUtil.getInternalIcon("de/apian/mathbase/images/icon.png", 64, 64));
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