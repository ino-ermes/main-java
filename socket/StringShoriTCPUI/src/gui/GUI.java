package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;

public class GUI extends JFrame {

    protected JPanel topPanel;
    protected JPanel botPanel;
    protected JTextArea textArea;
    protected static Font DEFAULT_FONT;

    static {
        DEFAULT_FONT = new Font("area", Font.PLAIN, 14);
    }

    public GUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        this.setLayout(new BorderLayout());
        this.add(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;

        topPanel = new JPanel(new GridBagLayout());
        botPanel = new JPanel(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        panel.add(topPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        panel.add(botPanel, c);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(DEFAULT_FONT);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(textArea);
        botPanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        botPanel.add(scrollPane, c);

        panel.setBackground(Color.DARK_GRAY);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
    }

    protected void writeLineTextArea(String content) {
        if (!content.isBlank()) {
            textArea.append(" $ " + content + "\n");
        }
    }
}
