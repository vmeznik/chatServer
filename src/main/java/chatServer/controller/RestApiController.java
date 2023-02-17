package chatServer.controller;

import chatServer.model.*;
import chatServer.repository.IUsersRepository;
import chatServer.service.MessageService;
import chatServer.socketServer.ClientHandler;
import chatServer.utility.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final IUsersRepository usersRepository;
    private final MessageService messageService;

    @PostMapping("chatClient/register")
    public RequestConfirmation registerUser(@RequestBody UserInfo userInfo) {
        return registerUserConfirmation(userInfo);
    }

    @PostMapping("chatClient/login")
    public RequestConfirmation loginUser(@RequestBody UserInfo userInfo) {
        return loginUserConfirmation(userInfo);
    }

    @PostMapping("chatClient/forgotPassword")
    public String forgotPasswordRequest(@RequestBody UserInfo userInfo) {
        return forgotPasswordFromRepository(userInfo);
    }


    private String forgotPasswordFromRepository(UserInfo userInfo) {
        List<Member> list = usersRepository.forgotPasswordFromDb(userInfo.getUserName(), userInfo.getEmail());
        if (!list.isEmpty()) {
            return list.get(0).getPassword();
        }
        return "User does not exists , check username and email";
    }

    private RequestConfirmation loginUserConfirmation(UserInfo userInfo) {
        System.out.println(userInfo.toString());
        if (!usersRepository.loginUser(userInfo.getUserName(), userInfo.getPassword()).isEmpty()) {
            if (!checkIfUserIsAlreadyLogged(userInfo.getUserName())) {
                return new RequestConfirmation(true, null);
            }
            return new RequestConfirmation(false, "User is already logged");
        }
        return new RequestConfirmation(false, "Wrong username or password");
    }

    private RequestConfirmation registerUserConfirmation(UserInfo userInfo) {
        try {
            if (!usersRepository.nameExists(userInfo.getUserName()).isEmpty()) {
                return new RequestConfirmation(false, "Username already exists");
            }
            if (!usersRepository.emailExists(userInfo.getEmail()).isEmpty()) {
                return new RequestConfirmation(false, "Email already exists");
            }
            Logger.getInstance().log(userInfo.getUserName() + " was registered");
            usersRepository.addUser(userInfo.getUserName(), userInfo.getPassword(), userInfo.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestConfirmation(false, "User was not registered");
        }
        return new RequestConfirmation(true, null);
    }

    private boolean checkIfUserIsAlreadyLogged(String username) {
        for (int i = 0; i < ClientHandler.getLoggedMembers().size(); i++) {
            if (ClientHandler.getLoggedMembers().get(i).getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
