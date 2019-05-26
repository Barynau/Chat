import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener tcpConnectionListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(final TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.tcpConnectionListener = tcpConnectionListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    tcpConnectionListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        tcpConnectionListener.onResceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {

                } finally {
                    tcpConnectionListener.onDisconect(TCPConnection.this);
                }
            }
        });
        rxThread.start();

    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String ipAddr, int port) throws IOException {
        this(tcpConnectionListener, new Socket(ipAddr, port));
    }

    @Override
    public String toString() {
        return "TCPConnection " + socket.getInetAddress() + ": " + socket.getPort();
    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            tcpConnectionListener.onExeption(TCPConnection.this, e);
            disconect();
        }
    }

    public synchronized void disconect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            tcpConnectionListener.onExeption(TCPConnection.this, e);
        }
    }
}
