package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class SocketClient {
    private int PORT;
    private String HOST;

    private DataOutputStream outputStream;

    Consumer<String> receiveHandler;

    private boolean isRunning;

    synchronized public boolean isRunning() {
        return isRunning;
    }

    synchronized public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public SocketClient(int port, String host, Consumer<String> receiveHandler) {
        PORT = port;
        HOST = host;
        this.receiveHandler = receiveHandler;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                setRunning(true);
                try (Socket socket = new Socket(HOST, PORT);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());) {

                    outputStream = out;
                    boolean isClosedFromServer = false;
                    while (isRunning()) {
                        String recieve = in.readUTF();
                        if (!isRunning())
                            break;
                        if (recieve.equals("/close")) {
                            isClosedFromServer = true;
                            setRunning(false);
                        } else {
                            receiveHandler.accept(recieve);
                        }
                    }
                    if (!isClosedFromServer)
                        out.writeUTF("/close");
                } catch (IOException e) {
                    //e.printStackTrace();
                } finally {
                    setRunning(false);
                    outputStream = null;
                }
            };
        }.start();
    }

    public void send(String content) {
        try {
            if (outputStream != null) {
                outputStream.writeUTF(content);
                outputStream.flush();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void close() {
        setRunning(false);
        if (outputStream != null) {
            try {
                outputStream.writeUTF("/close");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
