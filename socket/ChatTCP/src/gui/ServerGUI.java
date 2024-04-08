package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import tcp.ChatServer;

public class ServerGUI extends GUI {

    private JTextField portTextField;
    private JButton beginButton;
    private JButton stopButton;
    private JTextField senTextField;
    private JButton sendButton;

    private ChatServer chatServer;

    public ServerGUI() {
        portTextField = new JTextField();
        beginButton = new JButton("Begin");
        stopButton = new JButton("Close");
        senTextField = new JTextField();
        sendButton = new JButton("Send");

        portTextField.setFont(DEFAULT_FONT);
        beginButton.setFont(DEFAULT_FONT);
        stopButton.setFont(DEFAULT_FONT);
        senTextField.setFont(DEFAULT_FONT);
        sendButton.setFont(DEFAULT_FONT);

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

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        senTextField.setPreferredSize(new Dimension(0, 30));
        topPanel.add(senTextField, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        sendButton.setPreferredSize(new Dimension(125, 30));
        topPanel.add(sendButton, c);

        addEvents();

        beginButton.setEnabled(true);
        stopButton.setEnabled(false);
        sendButton.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addEvents() {
        beginButton.addActionListener((ActionEvent e) -> handlerBeginButtonClicked(e));
        stopButton.addActionListener((ActionEvent e) -> handlerStopButtonClicked(e));
        sendButton.addActionListener((ActionEvent e) -> handlerSendButtonClicked(e));
    }

    public void handlerBeginButtonClicked(ActionEvent e) {
        int port;
        try {
            port = Integer.parseInt(portTextField.getText());
        } catch (NumberFormatException exception) {
            return;
        }

        chatServer = new ChatServer(port, content -> {
            if (!content.isBlank()) {
                writeLineTextArea(content);
            }
        });

        chatServer.run();

        writeLineTextArea("Listening on port: " + port);

        beginButton.setEnabled(false);
        stopButton.setEnabled(true);
        sendButton.setEnabled(true);
    }

    public void handlerStopButtonClicked(ActionEvent e) {
        if (chatServer != null) {
            chatServer.stop();
            chatServer = null;

            beginButton.setEnabled(true);
            stopButton.setEnabled(false);
            sendButton.setEnabled(false);
        }
    }

    private void handlerSendButtonClicked(ActionEvent e) {
        String content = senTextField.getText();
        if (chatServer != null && !content.isBlank()) {
            writeLineTextArea("me: " + content);
            chatServer.send("server: "+ content);
            senTextField.setText("");
        }
    }

}
