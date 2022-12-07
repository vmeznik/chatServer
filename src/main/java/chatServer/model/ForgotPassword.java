package chatServer.model;

import java.io.Serializable;

public class ForgotPassword implements Serializable {
    private String userName;
    private String email;

    public ForgotPassword(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}