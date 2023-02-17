package chatServer.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "public")
public class Member implements Serializable {
    @Column(name = "name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = -1;

    @Override
    public String toString() {
        return userName;
    }
}
