package ojt.g1.connection;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkHelper {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private Runnable onConnect;
    private Runnable onDisconnect;
    private Runnable onMessagesEmpty;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    /**
     * Starts a server that listens for connections
     * @param port The port to listen on
     * @param listener Callback for received messages
     */
    public void startServer(int port, MessageListener listener) {
        new Thread(() -> {
            while (true) { // Infinite loop to restart after disconnection
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    socket = serverSocket.accept();
                    if (onConnect != null)
                        onConnect.run();

                    setupStreams();

                    String message;
                    while ((message = reader.readLine()) != null) {
                        if (listener != null) {
                            listener.onMessageReceived(message);
                        }
                        if (onMessagesEmpty != null)
                            onMessagesEmpty.run();
                    }

                    if (onDisconnect != null)
                        onDisconnect.run();
                } catch (IOException e) {}
            }
        }).start();
    }


    /**
     * Connects to a server as a client
     * @param ip The server's IP address
     * @param port The server's port
     */
    public void connectToServer(String ip, int port) {
        new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                writer = new PrintWriter(socket.getOutputStream(), true);

                while (running) {
                    String message = messageQueue.take(); // Waits for a message to send
                    writer.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sends a message through the socket
     * @param message The message to send
     */
    public void sendMessage(String message) {
        messageQueue.offer(message); // Add message to queue (non-blocking)
    }

    /**
     * Closes the connection
     */
    public void close() {
        running = false;
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void setEventOnDeviceConnect(Runnable onConnect) {
        this.onConnect = onConnect;
    }

    public void setEventOnDeviceDisconnect(Runnable onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    public void setEventOnMessagesEmpty(Runnable onMessagesEmpty) {
        this.onMessagesEmpty = onMessagesEmpty;
    }

    public String getDevice() {
        if (socket != null && socket.getInetAddress() != null) {
            return socket.getInetAddress() + "";
        }
        return "Unknown Device";
    }
}