import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class ChatServer implements ConnectionListener {
    public static void main(String[] args) {
new ChatServer();
    }

    private final ArrayList<Connection> connectionsList = new ArrayList<>();

    //возможно нуно 2 списка клиенты\агенты
    private ChatServer() {
        System.out.println("Server running ");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new Connection(this, serverSocket.accept());

                } catch (IOException e) {
                    System.out.println("Connection exception: " + e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void connectionReady(Connection connection) {

        connectionsList.add(connection);
        // сдесь будет логика сортировки клиент\агент
        sendToAll("Client connected:" + connection);

    }

    @Override
    public synchronized void onReciveString(Connection connection, String string) {
      sendToAll(string);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connectionsList.remove(connection);
    }

    @Override
    public synchronized void onExeption(Connection connection, Exception e) {
        System.out.println("Exception: " + e);
    }

    private void sendToAll(String string) {
        System.out.println(string);
        final int counter = connectionsList.size();
        for (int i = 0; i < counter; i++) {
            connectionsList.get(i).SendString(string);
        }
    }
}
