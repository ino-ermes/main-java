package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.function.Consumer;

public class UDPClient {
    private int PORT;
    private String HOST;

    private DatagramSocket socket;

    Consumer<String> receiveHandler;

    public UDPClient(int port, String host, Consumer<String> receiveHandler) {
        PORT = port;
        HOST = host;
        this.receiveHandler = receiveHandler;
    }

    public void start() {
        new Thread(() -> {
            try {
                socket = new DatagramSocket(0);
                byte[] receiveData = new byte[1024];

                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, 1024);
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");;

                    receiveHandler.accept(response); // print to textarea
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }

        }).start();
    }

    public void send(String content) {
        try {
            byte[] sendData = content.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
            sendPacket.setAddress(InetAddress.getByName(HOST));
            sendPacket.setPort(PORT);
            socket.send(sendPacket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if(socket != null) {
            socket.close();
        }
    }
}
