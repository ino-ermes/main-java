package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import tcp.SocketClient;

public class ClientGUI extends GUI {

    private JTextField portTextField;
    private JButton connectButton;
    private JButton stopButton;

    private JTextField senTextField;
    private JButton sendButton;

    private SocketClient socketClient;

    public ClientGUI() {
        portTextField = new JTextField();
        connectButton = new JButton("Begin");
        stopButton = new JButton("Close");
        senTextField = new JTextField();
        sendButton = new JButton("Send");

        portTextField.setFont(DEFAULT_FONT);
        connectButton.setFont(DEFAULT_FONT);
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
        connectButton.setPreferredSize(new Dimension(125, 30));
        topPanel.add(connectButton, c);

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

        connectButton.setEnabled(true);
        stopButton.setEnabled(false);
        sendButton.setEnabled(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void addEvents() {
        connectButton.addActionListener((ActionEvent e) -> handlerConnectButtonClicked(e));
        stopButton.addActionListener((ActionEvent e) -> handlerStopButtonClicked(e));
        sendButton.addActionListener((ActionEvent e) -> handlerSendButtonClicked(e));
    }

    private void handlerConnectButtonClicked(ActionEvent e) {
        int port;
        try {
            port = Integer.parseInt(portTextField.getText());
        } catch (NumberFormatException exception) {
            return;
        }

        socketClient = new SocketClient(port, "localhost", (String content) -> {
            if (!content.isBlank()) {
                writeLineTextArea(content);
            }
        });

        socketClient.start();

        connectButton.setEnabled(false);
        stopButton.setEnabled(true);
        sendButton.setEnabled(true);
    }

    private void handlerStopButtonClicked(ActionEvent e) {
        if (socketClient != null) {
            socketClient.close();
            socketClient = null;

            connectButton.setEnabled(true);
            stopButton.setEnabled(false);
            sendButton.setEnabled(false);
        }
    }

    private void handlerSendButtonClicked(ActionEvent e) {
        if (socketClient != null && !senTextField.getText().isBlank()) {
            socketClient.send(senTextField.getText());
            senTextField.setText("");
        }
    }
}
