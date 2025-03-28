package ojt.g1.connection;

import ojt.g1.input.Action;
import ojt.g1.input.Decode;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkHelper {

    public static ArrayList<NetworkEntry> networkEntries = new ArrayList<>();
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private Runnable onConnect;
    private Runnable onDisconnect;
    private Runnable onMessagesEmpty;
    private boolean running = false;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    /**
     * Starts a server that listens for connections
     * @param port The port to listen on
     */
    public void startServer(int port) {
        NetworkEntry networkEntry = new NetworkEntry();
        new Thread(() -> {
            while (true) { // Infinite loop to restart after disconnection
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    networkEntry.socket = serverSocket.accept();
                    if (onConnect != null)
                        onConnect.run();

                    setupStreams(networkEntry);

                    String message;
                    while ((message = networkEntry.reader.readLine()) != null) {
                        messageQueue.add(message);
                        System.out.println(message);
                        if (onMessagesEmpty != null)
                            onMessagesEmpty.run();
                    }

                    if (onDisconnect != null)
                        onDisconnect.run();
                } catch (IOException e) {}
            }
        }).start();
        networkEntries.add(networkEntry);
        readMessage();
    }

    public void readMessage() {
        if (running)
            return;

        running = true;

        new Thread(() -> {
            Action action = new Action();
            MessageListener messageListener = message -> {
                if (Decode.isInputType(message)) {
                    Decode.Code code = Decode.decode(message);
                    action.perform(Decode.decode(message));
                    System.out.println("Received: " + message + " Code: " + Decode.translate(code.getCode()) + " Tag: " + Decode.translate(code.getTag()));
                } else if (Decode.isMouseMove(message)) {
                    action.mouseMove(message);
                    System.out.println("Received: " + message);
                } else if (Decode.isMouseScroll(message)) {
                    action.scroll(message);
                    System.out.println("Received: " + message);
                } else if (Decode.isZoom(message)) {
                    action.zoom(message);
                    System.out.println("Received: " + message);
                }
            };

            while (true) {
                try {
                    messageListener.onMessageReceived(messageQueue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    /**
     * Connects to a server as a client
     * @param ip The server's IP address
     * @param port The server's port
     */
    public void connectToServer(String ip, int port) {
//        new Thread(() -> {
//            try {
//                socket = new Socket(ip, port);
//                writer = new PrintWriter(socket.getOutputStream(), true);
//
//                while (running) {
//                    String message = messageQueue.take(); // Waits for a message to send
//                    writer.println(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
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
    public void close(NetworkEntry networkEntry) {
        networkEntry.running = false;
        try {
            if (networkEntry.socket != null) networkEntry.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupStreams(NetworkEntry networkEntry) throws IOException {
        networkEntry.writer = new PrintWriter(networkEntry.socket.getOutputStream(), true);
        networkEntry.reader = new BufferedReader(new InputStreamReader(networkEntry.socket.getInputStream()));
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
        for (int i = 0; i < networkEntries.size(); i++) {
            NetworkEntry networkEntry = networkEntries.get(i);
            if (networkEntry.socket != null && networkEntry.socket.getInetAddress() != null) {
                return networkEntry.socket.getInetAddress() + "";
            }
        }
        return "Unknown Device";
    }

    public static class NetworkEntry {
        Socket socket;
        PrintWriter writer;
        BufferedReader reader;
        volatile boolean running = true;
    }
}