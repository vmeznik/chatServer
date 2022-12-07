package chatServer.model;

public class Message {
    private String receiver;
    private String sender;
    private String text;

    public Message(String receiver, String sender, String text) {
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return sender + " : " + text;
    }

}