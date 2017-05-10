package gui;

import util.ImageUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasse des Hauptmenüs
 */
public class MenuView extends AbstractView {

    public MenuView(MainFrame mainFrame){
        super(mainFrame);
        setLayout(new BorderLayout());
        initToolPane();
        initTopicList();
    }

    protected void update() {

    }

    /**
     * Hilfsklasse für die Initialisierung der Werkzeugleiste
     */
    private void initToolPane() {
        JPanel toolPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Hinzufügen");
        JLabel searchLabel = new JLabel("Suchen:");
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100, 20));
        toolPane.add(addButton);
        toolPane.add(searchLabel);
        toolPane.add(searchField);
        add(toolPane, BorderLayout.SOUTH);
    }

    /**
     * Hilfsklasse für die Initialisierung der Themenliste
     */
    private void initTopicList() {
        DefaultListModel<TopicListItem> model = new DefaultListModel<>();
        try {
            model.addElement(new TopicListItem("Rosenkohl", ImageUtil.getImage("images/icon.png")));
            model.addElement(new TopicListItem("Blattspinat", ImageUtil.getImage("images/icon.png")));
            model.addElement(new TopicListItem("Aubergine", ImageUtil.getImage("images/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JList<TopicListItem> topicList = new JList<>(model);
        topicList.setCellRenderer(new TopicListCellRenderer());
        topicList.setFixedCellHeight(50);
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(topicList);
        add(scrollPane, BorderLayout.CENTER);
    }
}

/**
 * Klassenhülle für ein Element der Themenliste
 */
class TopicListItem {
    private String title;
    private ImageIcon icon;

    TopicListItem(String title, BufferedImage icon) {
        this.title = title;
        Image image = new ImageIcon(icon).getImage();
        this.icon = new ImageIcon(image.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
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
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        label.setFont(label.getFont().deriveFont(16f));
    }

    /**
     * Dies ist die essentielle Methode, um die Darstellung der Listenelemente zu bearbeiten
     * @param list Betroffene JList
     * @param value Aktueller Listeneintrag
     * @param index Index des aktuellen Eintrags
     * @param selected Aktueller Eintrag ausgewählt
     * @param focused Aktueller Eintrag im Fokus
     * @return Listeneintrag als Swing-Komponente
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean focused) {
        TopicListItem item = (TopicListItem)value;
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