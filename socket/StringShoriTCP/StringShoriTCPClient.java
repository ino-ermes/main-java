import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class StringShoriTCPClient {
    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 7777);
                Scanner scanner = new Scanner(System.in);) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
                String requestString = scanner.nextLine();
                out.writeUTF(requestString);
                System.out.println(in.readUTF());
            }

        } catch (IOException e) {

        }

    }
}
