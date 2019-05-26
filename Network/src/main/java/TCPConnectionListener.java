public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);

    void onResceiveString(TCPConnection tcpConnection, String value);

    void onDisconect(TCPConnection tcpConnection);

    void onExeption(TCPConnection tcpConnection, Exception e);

}
