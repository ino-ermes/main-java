package upd;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

public class ChatServer {

    private static class Address {
        private String host;
        private int port;

        public Address(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((host == null) ? 0 : host.hashCode());
            result = prime * result + port;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Address other = (Address) obj;
            if (host == null) {
                if (other.host != null)
                    return false;
            } else if (!host.equals(other.host))
                return false;
            if (port != other.port)
                return false;
            return true;
        }
    };

    private final int port;
    private final static Set<Address> addresses;
    private final Consumer<String> outputHandler;

    private DatagramSocket serverSocket;

    public void stop() {

    }

    static {
        addresses = new HashSet<>();
    }

    public ChatServer(int port, Consumer<String> outputHandler) {
        this.port = port;
        this.outputHandler = outputHandler;
    }

    public void run() {
        new Thread(() -> {
            try {
                serverSocket = new DatagramSocket(port);
                byte[] receiveData = new byte[1024];

                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    Address address = new Address(receivePacket.getAddress().getHostName(), receivePacket.getPort());

                    synchronized (addresses) {
                        addresses.add(address);
                    }

                    String requestContent = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                    outputHandler.accept(address.getPort() + ": " + requestContent);
                    broadcast(address.getPort() + ": " + requestContent, address);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket.close();
            }

        }).start();
    }

    public void broadcast(String content, Address except) {
        new Thread() {
            @Override
            public void run() {
                synchronized (addresses) {
                    Iterator<Address> iterator = addresses.iterator();
                    while (iterator.hasNext()) {
                        Address address = iterator.next();
                        if (address.equals(except))
                            continue;
                        try {
                            byte[] sendData = content.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
                            sendPacket.setAddress(InetAddress.getByName(address.getHost()));
                            sendPacket.setPort(address.getPort());
                            serverSocket.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                            iterator.remove();
                        }
                    }
                }
            }
        }.start();

    }
}
