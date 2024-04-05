import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class StringShoriTCPServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(7777);) {

            while(!server.isClosed()) {

                try (Socket socket = server.accept()) {
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    
                    while (!socket.isClosed()) {
                        String requestString = in.readUTF();

                        String shoriString = requestString + "hello";

                        out.writeUTF(shoriString);
                        
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
}