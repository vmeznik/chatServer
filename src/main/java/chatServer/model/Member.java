package chatServer.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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
    private int id;

    public Member(String userName, String password, String email, int id) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public Member() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return userName;
    }
}
