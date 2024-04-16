package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JTextField;

import udp.UDPServer;

public class ServerGUI extends GUI {

    private JTextField portTextField;
    private JButton beginButton;
    private JButton stopButton;

    private UDPServer udpServer;

    public ServerGUI() {
        portTextField = new JTextField();
        beginButton = new JButton("Begin");
        stopButton = new JButton("Close");

        portTextField.setFont(DEFAULT_FONT);
        beginButton.setFont(DEFAULT_FONT);
        stopButton.setFont(DEFAULT_FONT);

        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        portTextField.setPreferredSize(new Dimension(0, 30));
        topPanel.add(portTextField, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.0;
        beginButton.setPreferredSize(new Dimension(125, 30));
        topPanel.add(beginButton, c);

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.0;
        stopButton.setPreferredSize(new Dimension(125, 30));
        topPanel.add(stopButton, c);

        beginButton.setEnabled(true);
        stopButton.setEnabled(false);

        addEvents();
    }

    private void addEvents() {
        beginButton.addActionListener((ActionEvent e) -> handlerBeginButtonClicked(e));
        stopButton.addActionListener((ActionEvent e) -> handlerStopButtonClicked(e));
    }

    public void handlerBeginButtonClicked(ActionEvent e) {
        int port;
        try {
            port = Integer.parseInt(portTextField.getText());
        } catch (NumberFormatException exception) {
            return;
        }

        udpServer = new UDPServer(port, (request) -> {
            // request == '/datetime'
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            return formattedDateTime;
        }, (String content) -> {
            if (!content.isBlank()) {
                writeLineTextArea(content);

            }
        });

        udpServer.start();

        writeLineTextArea("Listening on port: " + port);

        beginButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void handlerStopButtonClicked(ActionEvent e) {
        if (udpServer != null) {
            udpServer.close();
            udpServer = null;

            beginButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

}
