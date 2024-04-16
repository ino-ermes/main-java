package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;
import java.util.function.Function;

public class UDPServer {

    private final int PORT;
    private final Function<String, String> requestHandler;
    private final Consumer<String> receiveHandler;
    private DatagramSocket serverSocket;

    public UDPServer(int port, Function<String, String> requestHandler, Consumer<String> receiveHandler) {
        PORT = port;
        this.requestHandler = requestHandler;
        this.receiveHandler = receiveHandler;
    }

    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new DatagramSocket(PORT);
                byte[] receiveData = new byte[1024];

                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String requestContent = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");;

                    receiveHandler.accept(requestContent); // print to textarea
                    String result = requestHandler.apply(requestContent); // string process

                    byte[] sendData = result.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
                    sendPacket.setAddress(receivePacket.getAddress());
                    sendPacket.setPort(receivePacket.getPort());
                    serverSocket.send(sendPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket.close();
            }

        }).start();
    }

    public void close() {
        if(serverSocket != null) {
            serverSocket.close();
        }
    }
}
