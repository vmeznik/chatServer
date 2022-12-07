package chatServer.utility;


import chatServer.model.Message;

import java.util.ArrayList;

public class ConvList {
    private final String sender;
    private final String receiver;
    private final ArrayList<Message> list;

    public ConvList(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        list = new ArrayList<>();
    }

    public ArrayList<Message> getList() {
        return list;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void addToChat(Message message) {
        list.add(message);
    }
}
