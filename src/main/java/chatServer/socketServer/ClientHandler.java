package chatServer.socketServer;

import chatServer.model.Member;
import chatServer.model.Message;
import chatServer.service.MessageService;
import chatServer.utility.Logger;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> ClientHandlers = new ArrayList<>();
    public static ArrayList<Member> members = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientUsername;
    private String selectedChat;
    private Gson gson;
    private MessageService messageService;
    private Member thisMember;
    private static Member allChat;

    public void init(Socket socket) {
        this.gson = new Gson();
        messageService = MessageService.getMessageService();
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.clientUsername = gson.fromJson(in.readLine(), Message.class).getSender();
            addAllChat();
            this.thisMember = new Member(this.clientUsername, null, null, -1);
            ClientHandlers.add(this);
            members.add(thisMember);
            Logger.getInstance().log(clientUsername + " Logged in");
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
        List<Message> chat;
        String toClient;
        String fromClient;
        while (socket.isConnected()) {
            try {
                fromClient = in.readLine();
                message = gson.fromJson(fromClient, Message.class);
                if (emptyMsgFilter(message)) {
                    messageService.addMessage(message);
                }
                if (message.getSender().equals(this.clientUsername)) {
                    selectedChat = message.getReceiver();
                    chat = messageService.getConversation(message.getSender(), message.getReceiver());
                    toClient = gson.toJson(chat);
                    out.println(toClient);
                    if (message.getReceiver().equals("all")) {
                        updateAllChat(toClient);
                    } else {
                        sendToReceiver(message, toClient);
                    }
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
        for (ClientHandler ClientHandler : ClientHandlers) {
            ClientHandler.send(s);
        }
    }

    public String getSelectedChat() {
        return this.selectedChat;
    }

    private void sendToReceiver(Message message, String toClient) {
        try {
            for (ClientHandler ClientHandler : ClientHandlers) {
                if (ClientHandler.getClientUsername().equals(message.getReceiver())
                        && ClientHandler.getSelectedChat().equals(this.clientUsername)) {
                    ClientHandler.send(toClient);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void updateAllChat(String toClient) {
        try {
            for (ClientHandler ClientHandler : ClientHandlers) {
                if (ClientHandler.getSelectedChat().equals("all")) {
                    ClientHandler.send(toClient);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }


    private void removeClientHandler() {
        ClientHandlers.remove(this);
    }

    private void removeMember() {
        members.remove(thisMember);
        Logger.getInstance().log(clientUsername + " Logged out");
        sendToAll(gson.toJson(members));
    }

    private void addAllChat() {
        if (allChat == null) {
            allChat = new Member("all", null, null, -1);
            members.add(allChat);
        }
    }


    private boolean emptyMsgFilter(Message message) {
        return !message.getText().equals("") && message.getText() != null;
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
