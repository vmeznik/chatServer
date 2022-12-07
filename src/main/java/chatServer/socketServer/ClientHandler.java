package chatServer.socketServer;

import chatServer.model.Member;
import chatServer.model.Message;
import chatServer.utility.Conversations;
import chatServer.utility.Logger;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<Member> members = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientUsername;
    private String selectedChat;
    private final Gson gson;
    public static  Conversations conversations;
    private Member thisMember;
    private static Member allChat;

    public ClientHandler(Socket socket) {
        this.gson = new Gson();
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.clientUsername = gson.fromJson(in.readLine(), Message.class).getSender();
            conversations = Conversations.getInstance();
            addAllChat();
            this.thisMember = new Member(this.clientUsername, null, null, -1);
            clientHandlers.add(this);
            members.add(thisMember);
            Logger.getInstance().log(clientUsername+" Logged in");
            sendToAll(gson.toJson(members));
        } catch (IOException e) {
            closeEverything(socket, in, out);
        }
    }

    public String getClientUsername() {
        return this.clientUsername;
    }

    @Override
    public void run() {
        Message message;
        ArrayList<Message> chat;
        String toClient;
        String fromClient;
        while (socket.isConnected()) {
            try {
                fromClient = in.readLine();
                message = gson.fromJson(fromClient, Message.class);
                if (message.getSender().equals(this.clientUsername)) {
                    selectedChat = message.getReceiver();
                    chat = conversations.getConv(message);
                    toClient = gson.toJson(chat);
                    out.println(toClient);
                    sendToReceiver(message, toClient);
                }
            } catch (IOException e) {
                closeEverything(socket, in, out);
                break;
            }
        }

    }

    public void send(String s) {
        out.println(s);
    }

    public void sendToAll(String s) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(s);
        }
    }

    public String getSelectedChat() {
        return this.selectedChat;
    }

    private void sendToReceiver(Message message, String toClient) {
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler.getClientUsername().equals(message.getReceiver())
                        && clientHandler.getSelectedChat().equals(this.clientUsername)) {
                    clientHandler.send(toClient);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void removeClientHandler() {
        clientHandlers.remove(this);
    }

    private void removeMember() {
        members.remove(thisMember);
        Logger.getInstance().log(clientUsername+" Logged out");
        sendToAll(gson.toJson(members));
    }

    private void addAllChat() {
        if (allChat == null) {
            allChat = new Member("all", null, null, -1);
            members.add(allChat);
        }
    }

    public static ArrayList<Member> getLoggedMembers() {
        return members;
    }

    private void closeEverything(Socket socket, BufferedReader in, PrintWriter out) {
        removeClientHandler();
        removeMember();
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
