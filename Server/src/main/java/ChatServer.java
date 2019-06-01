import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final ArrayList<TCPConnection> connectionsAgents = new ArrayList<>();
    private final ArrayList<TCPConnection> connectionsClients = new ArrayList<>();


    public static void main(String[] args) {
        new ChatServer();
    }

    private ChatServer() {
        System.out.println("Server runing");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());

                } catch (IOException e) {
                    System.out.println("TCPConnectioexception " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
              connections.add(tcpConnection);

    }

    @Override
    public synchronized void onResceiveString(TCPConnection tcpConnection, String value) {
        // проверка клиент\агент, поделючение к агенту клиента
    }

    @Override
    public synchronized void onDisconect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        SendToAllConnections("Clent disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onExeption(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exeption: " + e);
    }

    private void SendToAllConnections(String value) {
        System.out.println(value);
        final int counter = connections.size();
        for (int i = 0; i < counter; i++) {
            connections.get(i).sendString(value);
        }
    }
}
