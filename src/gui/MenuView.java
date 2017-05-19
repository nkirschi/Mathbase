package gui;

import util.ElementDataHandler;
import util.ImageUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

import static util.Logger.*;

/**
 * Klasse des Hauptmenüs
 */
public class MenuView extends AbstractView {
    private DefaultListModel<TopicListItem> listmodel;
    private JList<TopicListItem> list;
    private static MenuView instance;

    private MenuView(MainFrame mainFrame) {
        super(mainFrame);
        setLayout(new BorderLayout());
        initToolPane();
        initTopicList();
        log(INFO, "Oberfläche vollständig initialisiert");
    }

    public static MenuView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new MenuView(mainFrame);
        return instance;
    }

    protected void update() {
        listmodel.clear();
        initTopicListModel();
    }

    /**
     * Hilfsmethode für die Initialisierung der Werkzeugleiste
     */
    private void initToolPane() {
        JPanel toolPane = new JPanel(new BorderLayout());

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Hinzufügen");
        addButton.addActionListener(a -> new AddTopicDialog(mainFrame));
        JButton removeButton = new JButton("Entfernen");
        removeButton.addActionListener(a -> {
            if (list.getSelectedValue() == null)
                JOptionPane.showMessageDialog(this, "Kein Thema gewählt!", "Thema löschen",
                        JOptionPane.ERROR_MESSAGE);
            else {
                int result = JOptionPane.showConfirmDialog(this, String.format("Möchten Sie dieses " +
                                "Thema wirklich unwiderruflich löschen?%nDiese Aktion " +
                                "kann nicht rückgängig gemacht werden!%n%n%s%n%n",
                        list.getSelectedValue().getTitle()), "Thema löschen",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == 0) {
                    String key = list.getSelectedValue().getKey();
                    try {
                        FileUtils.deleteDirectory(new File("topics/" + key));
                    } catch (IOException e) {
                        log(WARNING, "Konnte Ordner des Themas " + key + " nicht löschen!");
                        log(WARNING,e);
                    }
                    ElementDataHandler.getElementDataHandler().deleteTheme(key);
                    ElementDataHandler.getElementDataHandler().safeElementData();
                    this.update();
                }
            }
        });
        try {
            addButton.setIcon(ImageUtil.getInternalIcon("images/add.png", 12, 12));
            removeButton.setIcon(ImageUtil.getInternalIcon("images/remove.png", 12, 12));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addButton.setIconTextGap(MainFrame.BUTTON_ICON_TEXT_GAP);
        addButton.setMargin(MainFrame.BUTTON_INSETS);
        removeButton.setIconTextGap(MainFrame.BUTTON_ICON_TEXT_GAP);
        removeButton.setMargin(MainFrame.BUTTON_INSETS);

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField();
        JLabel searchLabel = new JLabel("Suche:");
        searchLabel.setLabelFor(searchField);
        searchField.setPreferredSize(new Dimension(200, 22));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (searchField.getText().equals("")) {
                    update();
                    return;
                }
                listmodel.clear();
                initTopicListModel(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (searchField.getText().equals("")) {
                    update();
                    return;
                }
                listmodel.clear();
                initTopicListModel(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if (searchField.getText().equals("")) {
                    update();
                    return;
                }
                listmodel.clear();
                initTopicListModel(searchField.getText());
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        toolPane.add(searchPanel, BorderLayout.EAST);

        buttonPane.add(addButton);
        buttonPane.add(removeButton);


        /*
        JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Suchen:");
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 22));
        searchPane.add(searchLabel);
        searchPane.add(searchField);
        toolPane.add(searchPane, BorderLayout.EAST);
        */

        toolPane.add(buttonPane, BorderLayout.WEST);
        add(toolPane, BorderLayout.SOUTH);
    }

    /**
     * Hilfsmethode für die Initialisierung der Themenliste
     */
    private void initTopicList() {
        listmodel = new DefaultListModel<>();
        initTopicListModel();
        JList<TopicListItem> topicList = new JList<>(listmodel);
        topicList.setCellRenderer(new TopicListCellRenderer());
        topicList.setFixedCellHeight(50);
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2) {
                    TopicListItem item = (TopicListItem) ((JList) mouseEvent.getSource()).getSelectedValue();
                    mainFrame.changeTo(TopicView.getInstance(mainFrame, item.getKey()));
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(topicList);
        add(scrollPane, BorderLayout.CENTER);
        list = topicList;
    }

    private void initTopicListModel() {
        try {
            ElementDataHandler handler = ElementDataHandler.getElementDataHandler();
            String[] keys = handler.getTopicKeyList();
            ArrayList<TopicListItem> items = new ArrayList<>(keys.length);
            for (String s : keys) {
                ImageIcon icon;
                try {
                    icon = ImageUtil.getExternalIcon(handler.getTopicIconPath(s));
                } catch (IOException e1) {
                    log(WARNING, "Konnte externes Bild nicht laden, lade Standart-Icon");
                    icon = ImageUtil.getInternalIcon("images/icon.png");
                }
                items.add(new TopicListItem(s, handler.getTopicName(s), icon));
            }
            Collections.sort(items);
            for (TopicListItem i : items)
                listmodel.addElement(i);
        } catch (IOException e) {
            log(WARNING,e);
        }
    }

    private void initTopicListModel(String keyword) {
        try {
            ElementDataHandler handler = ElementDataHandler.getElementDataHandler();
            String[] keys = handler.getTopicKeyList();
            ArrayList<TopicListItem> items = new ArrayList<>(keys.length);
            for (String s : keys) {
                if (handler.getTopicName(s).toLowerCase().contains(keyword.toLowerCase())) {
                    ImageIcon icon;
                    try {
                        icon = ImageUtil.getExternalIcon(handler.getTopicIconPath(s));
                    } catch (IOException e1) {
                        log(WARNING, "Konnte externes Bild nicht laden, lade Standart-Icon");
                        icon = ImageUtil.getInternalIcon("images/icon.png");
                    }
                    items.add(new TopicListItem(s, handler.getTopicName(s), icon));
                }
            }
            Collections.sort(items);
            for (TopicListItem i : items)
                listmodel.addElement(i);
        } catch (IOException e) {
            log(WARNING,e);
        }
    }
}

/**
 * Klassenhülle für ein Element der Themenliste
 */
class TopicListItem implements Comparable {
    private String key, title;
    private ImageIcon icon;

    TopicListItem(String key, String title, ImageIcon icon) {
        this.key = key;
        this.title = title;
        this.icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }

    String getKey() {
        return key;
    }

    String getTitle() {
        return title;
    }

    ImageIcon getIcon() {
        return icon;
    }

    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Object o) {
        return getTitle().compareTo(((TopicListItem) o).getTitle());
    }
}

/**
 * Unterklasse des DefaultListCellRenderers, um benutzerdefinierte Listeneinträge zu realisieren
 */
class TopicListCellRenderer extends DefaultListCellRenderer {
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = new Color(163, 202, 232);
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;

    TopicListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        label.setIconTextGap(10);
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        label.setFont(label.getFont().deriveFont(16f));
    }

    /**
     * Dies ist die essentielle Methode, um die Darstellung der Listenelemente zu bearbeiten
     *
     * @param list     Betroffene JList
     * @param value    Aktueller Listeneintrag
     * @param index    Index des aktuellen Eintrags
     * @param selected Aktueller Eintrag ausgewählt
     * @param focused  Aktueller Eintrag im Fokus
     * @return Listeneintrag als Swing-Komponente
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean focused) {
        TopicListItem item = (TopicListItem) value;
        label.setIcon(item.getIcon());
        label.setText(item.getTitle());
        label.setToolTipText("Index in list: " + index);

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }
        return label;
    }
}