package chatServer.model;

import java.io.Serializable;

public class Register implements Serializable {
    private String userName;
    private String password;
    private String emailAddress;

    public Register(String userName, String password, String emailAddress) {
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
