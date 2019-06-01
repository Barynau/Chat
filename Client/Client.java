public class Client implements TCPConnectionListener {
    private final boolean status;//tru/falce agent/client
    private final String name;
    private TCPConnection connection;

    public Client(boolean status, String name) {
        this.status = status;
        this.name = name;
    }

    public static void main(String[] args) {
        
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

    }

    @Override
    public void onResceiveString(TCPConnection tcpConnection, String value) {

    }

    @Override
    public void onDisconect(TCPConnection tcpConnection) {

    }

    @Override
    public void onExeption(TCPConnection tcpConnection, Exception e) {

    }

    private synchronized void SendMessage(String msg) {

    }
}
