package chatServer.socketServer;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer {
    private final int PORT = 5678;
    private ServerSocket welcomeSocket;

    public SocketServer() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        System.out.println("Starting sever..");
        welcomeSocket = new ServerSocket(PORT);
        Socket socket;
        try {
            while (!welcomeSocket.isClosed()) {
                System.out.println("Waiting for client ..");
                //server set up
                socket = welcomeSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            closeWelcomeSocket();
        }
    }

    public void closeWelcomeSocket() {
        try {
            if (welcomeSocket != null) {
                welcomeSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
