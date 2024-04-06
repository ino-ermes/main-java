package tcp;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private final int port;
    private final String host;
    private final Consumer<String> outputHandler;
    private DataOutputStream out;

    public ChatClient(int port, String host, Consumer<String> outputHandler) {
        this.port = port;
        this.host = host;
        this.outputHandler = outputHandler;
    }

    public void run() {
        ChatClient that = this;
        new Thread() {
            @Override
            public void run() {
                try (
                        Socket socket = new Socket(host, port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());) {
                    that.out = out;
                    outputHandler.accept("Connected to server.");

                    String serverResponse;
                    while ((serverResponse = in.readUTF()) != null) {
                        outputHandler.accept(serverResponse);
                        if (serverResponse.equals("/close"))
                            break;
                    }
                } catch (IOException e) {
                    if (closedFromServer) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void send(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            if (closedFromServer) {
                e.printStackTrace();
            }
        }
    }

    private volatile boolean closedFromServer = false;

    public void close() {
        closedFromServer = true;
        send("/close");
    }

}
