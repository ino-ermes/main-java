package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import service.StringService;
import tcp.SocketFactory;

public class ServerGUI extends GUI {

    private JTextField portTextField;
    private JButton beginButton;
    private JButton stopButton;

    private SocketFactory socketFactory;

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

        socketFactory = new SocketFactory(port, StringService::stringProcesses, (String content) -> {
            if (!content.isBlank()) {
                writeLineTextArea(content);

            }
        });

        socketFactory.start();

        writeLineTextArea("Listening on port: " + port);

        beginButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void handlerStopButtonClicked(ActionEvent e) {
        if (socketFactory != null) {
            socketFactory.close();
            socketFactory = null;

            beginButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

}
