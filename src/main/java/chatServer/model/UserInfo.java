package chatServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserInfo implements Serializable {
    private String userName;
    private String email;
    private String password;
}