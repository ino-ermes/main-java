import gui.ClientGUI;
import gui.ServerGUI;

public class App {
    public static void main(String[] args) throws Exception {
        new ServerGUI().setVisible(true);
        new ClientGUI().setVisible(true);
        new ClientGUI().setVisible(true);
    }
}
