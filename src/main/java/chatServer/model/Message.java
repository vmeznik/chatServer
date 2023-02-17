package chatServer.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "messages", schema = "public")
@NoArgsConstructor
public class Message {
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "sender")
    private String sender;
    @Column(name = "text")
    private String text;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Message(String sender, String receiver, String text) {
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
    }

    @Override
    public String toString() {
        return sender + " : " + text;
    }
}