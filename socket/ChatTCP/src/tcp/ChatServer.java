package tcp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

public class ChatServer {
    private final int port;
    private final static Set<DataOutputStream> outs;
    private final Consumer<String> outputHandler;

    private volatile boolean running = true;

    public void stop() {
        running = false;
        synchronized (outs) {
            for (DataOutputStream out : outs) {
                try {
                    out.writeUTF("/close");
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outs.clear();
        }
    }

    static {
        outs = new HashSet<>();
    }

    public ChatServer(int port, Consumer<String> outputHandler) {
        this.port = port;
        this.outputHandler = outputHandler;
    }

    public void run() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                outputHandler.accept("Server started. Listening on port " + port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    outputHandler.accept("New client connected: " + clientSocket.getPort());

                    new Thread(new ClientHandler(clientSocket, outputHandler)).start();
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private DataOutputStream out;
        private final Consumer<String> outputHandler;

        public ClientHandler(Socket socket, Consumer<String> outputHandler) {
            this.clientSocket = socket;
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            try (DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());) {

                this.out = out;
                synchronized (outs) {
                    outs.add(out);
                }

                String inputLine;
                String broadcastContent;
                while ((inputLine = in.readUTF()) != null) {
                    broadcastContent = clientSocket.getPort() + ": " + inputLine;
                    outputHandler.accept(broadcastContent);
                    if (inputLine.equals("/close"))
                        break;
                    broadcast(broadcastContent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (this.out != null) {
                    synchronized (outs) {
                        outs.remove(this.out);
                    }
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) throws IOException {
            synchronized (outs) {
                for (DataOutputStream out : outs) {
                    if (this.out != out) {
                        if (!message.equals("/close")) {
                            out.writeUTF(message);
                            out.flush();
                        }
                    }
                }
            }
        }
    }
}
