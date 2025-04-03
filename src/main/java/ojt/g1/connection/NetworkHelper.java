package ojt.g1.connection;

import ojt.g1.input.Action;
import ojt.g1.input.Decode;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class NetworkHelper {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    public static final ArrayList<NetworkEntry> networkEntries = new ArrayList<>();
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private Runnable onConnect;
    private Runnable onDisconnect;
    private Runnable onMessagesEmpty;
    private volatile boolean running = false;
    private volatile boolean serverAvailable = false;
    private Action action = new Action();

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    /**
     * Starts a server that listens for connections
     * @param port The port to listen on
     */
    public void startServer(int port) {
        if (serverAvailable) return; // Prevent multiple starts

        serverAvailable = true;

        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port: " + port);

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New device connected: " + clientSocket.getInetAddress());

                    if (onConnect != null) onConnect.run();

                    NetworkEntry networkEntry = new NetworkEntry(clientSocket);
                    networkEntries.add(networkEntry);

                    handleClient(networkEntry);
                }
            } catch (IOException e) {
                System.out.println("Server stopped.");
            } finally {
                stopServer();
            }
        });
    }

    private void handleClient(NetworkEntry networkEntry) {
        executor.submit(() -> {
            try {
                String message;
                while ((message = networkEntry.reader.readLine()) != null) {
                    messageQueue.put(message);
                    if (onMessagesEmpty != null) onMessagesEmpty.run();
                }
            } catch (IOException | InterruptedException ignored) {

            } finally {
                close(networkEntry);
                if (onDisconnect != null) onDisconnect.run();
            }
        });

        readMessage();
    }

    public void readMessage() {
        if (running) return;

        running = true;

        executor.submit(() -> {
            try {
                while (!networkEntries.isEmpty()) {
                    MessageListener messageListener = message -> {
                        if (Decode.isInputType(message)) {
                            Decode.Code code = Decode.decode(message);
                            action.perform(code);
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
                        } else if (Decode.isShortCut(message)) {
                            action.performShortCut(message);
                            System.out.println("Received: " + message);
                        }
                    };
                    messageListener.onMessageReceived(messageQueue.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                running = false;
            }
        });
    }

    /**
     * Stops the server and closes all connections.
     */
    public void stopServer() {
        serverAvailable = false;
        for (NetworkEntry entry : new ArrayList<>(networkEntries)) {
            close(entry);
        }
        executor.shutdown(); // Stop accepting new tasks
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            executor.shutdownNow();
        }
    }

    /**
     * Sends a message through all connected clients
     * @param message The message to send
     */
    public void sendMessage(String message) {
        for (NetworkEntry entry : networkEntries) {
            entry.sendMessage(message);
        }
    }

    /**
     * Closes the client connection
     */
    public void close(NetworkEntry networkEntry) {
        networkEntries.remove(networkEntry); // Remove before closing to prevent concurrency issues
        try {
            if (networkEntry.socket != null) networkEntry.socket.close();
            if (networkEntry.reader != null) networkEntry.reader.close();
            if (networkEntry.writer != null) networkEntry.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        public NetworkEntry(Socket socket) throws IOException {
            this.socket = socket;
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}
