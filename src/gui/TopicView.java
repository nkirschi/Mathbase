package gui;

import javax.swing.*;

/**
 * Klasse für die Detailansicht der einzelnen Themen
 */
public class TopicView extends AbstractView {

    public TopicView(MainFrame mainFrame) {
        super(mainFrame);
        JButton backButton = new JButton("< Zurück");
        backButton.addActionListener(e -> mainFrame.changeTo(MenuView.getInstance(mainFrame)));
        add(backButton);
    }

    public void update() {

    }
}
