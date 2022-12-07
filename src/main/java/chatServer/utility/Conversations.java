package chatServer.utility;


import chatServer.model.Message;

import java.util.ArrayList;

public class Conversations {

    private final ArrayList<ConvList> db;
    private static Conversations instance;

    private Conversations() {
        this.db = new ArrayList<>();
        ConvList allChat = new ConvList("all", "all");
        allChat.addToChat(new Message("", "", "Start of the conversation"));
        db.add(allChat);
    }

    public static Conversations getInstance() {
        if (instance == null) {
            instance = new Conversations();
        }
        return instance;
    }


    public ArrayList<Message> getConv(Message message) {
        if (message.getReceiver().equals("all")) {
            if (emptyMessageFilter(message)) {
                db.get(0).addToChat(message);
            }
            return db.get(0).getList();
        }
        ArrayList<Message> existingList = findIfConvExists(message);
        if (existingList != null) {
            return existingList;
        }
        ConvList newList = new ConvList(message.getSender(), message.getReceiver());
        newList.addToChat(new Message("", "", "Start of the conversation"));
        if (emptyMessageFilter(message)) {
            newList.addToChat(message);
        }
        db.add(newList);
        return newList.getList();
    }

    private boolean emptyMessageFilter(Message message) {
        if (message.getText().equals("")) {
            return false;
        }
        return true;
    }

    private ArrayList<Message> findIfConvExists(Message message) {
        for (int i = 0; i < db.size(); i++) {
            if ((db.get(i).getReceiver().equals(message.getReceiver()) &&
                    db.get(i).getSender().equals(message.getSender()))
                    || (db.get(i).getReceiver().equals(message.getSender()) &&
                    db.get(i).getSender().equals(message.getReceiver()))) {
                if (emptyMessageFilter(message)) {
                    db.get(i).addToChat(message);
                }
                return db.get(i).getList();
            }
        }
        return null;
    }

}
