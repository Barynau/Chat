import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static com.sun.xml.internal.stream.writers.XMLStreamWriterImpl.UTF_8;

public class Connection {

    public final Socket socket;
    private final Thread rxThread;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;


    public Connection(ConnectionListener listener, String ip, int port) throws IOException {
        this(listener, new Socket(ip, port));
    }

    public Connection(final ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName(UTF_8)));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName(UTF_8)));
        rxThread = new Thread(new Runnable() {
            public void run() {
                try {
                    listener.connectionReady(Connection.this);
                    while (!rxThread.isInterrupted()) {
                        Connection.this.listener.onReciveString(Connection.this, in.readLine());
                    }

                } catch (IOException e) {
                    Connection.this.listener.onExeption(Connection.this, e);
                } finally {
                    Connection.this.listener.onDisconnect(Connection.this);
                }
            }
        });
    }

    public void SendString(String string) {
        try {
            out.write(string + "\r\n");
            out.flush();
        } catch (IOException e) {
            listener.onExeption(Connection.this, e);
            disconnect();
        }
    }

    public void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.onDisconnect(Connection.this);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Connection{" +
                "socket=" + socket +
                ", rxThread=" + rxThread +
                "}";
    }
}
