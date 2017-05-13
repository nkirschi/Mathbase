package gui;

import util.ElementDataHandler;
import util.ImageUtil;
import util.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Klasse des Hauptmenüs
 */
public class MenuView extends AbstractView {
    private DefaultListModel<TopicListItem> listmodel;
    private JList<TopicListItem> list;

    public MenuView(MainFrame mainFrame){
        super(mainFrame);
        setLayout(new BorderLayout());
        initToolPane();
        initTopicList();
        Logger.log(Logger.INFO, "Oberfläche vollständig initialisiert");
    }

    protected void update() {
        listmodel.clear();
        initTopicListModel(listmodel);
    }

    /**
     * Hilfsklasse für die Initialisierung der Werkzeugleiste
     */
    private void initToolPane() {
        JPanel toolPane = new JPanel(new BorderLayout());

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Hinzufügen");
        addButton.addActionListener(e -> new TopicDialog(mainFrame));
        JButton removeButton = new JButton("Entfernen");
        removeButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Wollen das Thema '"+list.getSelectedValue().getTitle()+
                    "' und alle enthalteten Elemente wirklich löschen (permanent!)?", "", JOptionPane.YES_NO_OPTION);
            if(result==0){
                ElementDataHandler.getElementDataHandler().deleteTheme(list.getSelectedValue().getKey());
                ElementDataHandler.getElementDataHandler().safeElementData();
                this.update();
            }
        });
        try {
            addButton.setIcon(ImageUtil.getIcon("images/add.png", 12, 12));
            removeButton.setIcon(ImageUtil.getIcon("images/remove.png", 12, 12));
        } catch (IOException e) {
            e.printStackTrace();
        }


        buttonPane.add(addButton);
        buttonPane.add(removeButton);

        JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Suchen:");
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 22));
        searchPane.add(searchLabel);
        searchPane.add(searchField);

        toolPane.add(buttonPane, BorderLayout.WEST);
        toolPane.add(searchPane, BorderLayout.EAST);
        add(toolPane, BorderLayout.SOUTH);
    }

    /**
     * Hilfsklasse für die Initialisierung der Themenliste
     */
    private void initTopicList() {
        DefaultListModel<TopicListItem> model = new DefaultListModel<>();
        initTopicListModel(model);
        JList<TopicListItem> topicList = new JList<>(model);
        topicList.setCellRenderer(new TopicListCellRenderer());
        topicList.setFixedCellHeight(50);
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() > 1) {
                    TopicListItem item = (TopicListItem)((JList)mouseEvent.getSource()).getSelectedValue();
                    System.out.printf("Titel: %s, Key: %s%n", item.getTitle(), item.getKey());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(topicList);
        add(scrollPane, BorderLayout.CENTER);
        listmodel=model;
        list=topicList;
    }

    private void initTopicListModel(DefaultListModel<TopicListItem> model){
        try {
            /*model.addElement(new TopicListItem("Rosenkohl", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Blattspinat", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Aubergine", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Gurke", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Wirsing", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Spargel", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Kartoffel", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Brokkoli", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Tomate", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Paprika", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Zucchini", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Rucola", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Meerrettich", ImageUtil.getIcon("images/cherry.png")));
            model.addElement(new TopicListItem("Knoblauch", ImageUtil.getIcon("images/icon.png")));
            model.addElement(new TopicListItem("Radieschen", ImageUtil.getIcon("images/cherry.png")));*/
            ElementDataHandler handler = ElementDataHandler.getElementDataHandler();
            for (String s : handler.getThemeKeyList())
                model.addElement(new TopicListItem(s, handler.getTopicName(s), ImageUtil.getIcon("images/cherry.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Klassenhülle für ein Element der Themenliste
 */
class TopicListItem {
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