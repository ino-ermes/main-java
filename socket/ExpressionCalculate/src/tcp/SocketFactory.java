package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class SocketFactory {

    private final int PORT;
    private final ExecutorService executor;
    private final Function<String, String> requestHandler;
    private final Consumer<String> receiveHandler;
    private static boolean isRunning;
    private ServerSocket server;

    synchronized public static boolean isRunning() {
        return SocketFactory.isRunning;
    }

    synchronized public static void setRunning(boolean isRunning) {
        SocketFactory.isRunning = isRunning;
    }

    public SocketFactory(int port, Function<String, String> requestHandler, Consumer<String> receiveHandler) {
        PORT = port;
        this.requestHandler = requestHandler;
        this.receiveHandler = receiveHandler;
        executor = Executors.newCachedThreadPool();
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                setRunning(true);
                try {
                    server = new ServerSocket(PORT);
                    while (isRunning()) {
                        Socket socket = server.accept();
                        executor.submit(new ClientHandler(socket, requestHandler, receiveHandler));
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } finally {
                    executor.shutdown();
                    setRunning(false);
                }
            };

        }.start();
    }

    public void close() {
        executor.shutdown();
        setRunning(false);
        try {
            server.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final Function<String, String> requestHandler;
        private final Consumer<String> receiveHandler;

        public ClientHandler(Socket socket, Function<String, String> requestHandler, Consumer<String> receiveHandler) {
            this.socket = socket;
            this.requestHandler = requestHandler;
            this.receiveHandler = receiveHandler;
        }

        @Override
        public void run() {
            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream())) {

                boolean isStopFromCLient = false;
                while (isRunning()) {
                    String recieve = in.readUTF();
                    if (!isRunning())
                        break;
                    if (recieve.equals("/close")) {
                        isStopFromCLient = true;
                        socket.close();
                        break;
                    } else {
                        receiveHandler.accept(recieve);
                        out.writeUTF(requestHandler.apply(recieve));
                    }
                }
                if (!isStopFromCLient)
                    out.writeUTF("/close");
            } catch (IOException e) {
                //e.printStackTrace();
            } finally {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }
    }
}
