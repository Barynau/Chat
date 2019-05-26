public interface ConnectionListener {
    void connectionReady(Connection connection);
    void onReciveString(Connection connection,String string);
    void onDisconnect(Connection connection);
    void onExeption (Connection connection,Exception e);


}
