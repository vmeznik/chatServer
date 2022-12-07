package chatServer.controller;

import chatServer.model.*;
import chatServer.repository.UsersRepository;
import chatServer.socketServer.ClientHandler;
import chatServer.utility.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestApiController {
    private final UsersRepository usersRepository;

    public RestApiController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping("chatClient/register")
    public RequestConfirmation registerUser(@RequestBody Register register) {
        return registerUserConfirmation(register);
    }

    @PostMapping("chatClient/login")
    public RequestConfirmation loginUser(@RequestBody Login login) {
        return loginUserConfirmation(login);
    }

    @PostMapping("chatClient/forgotPassword")
    public String forgotPasswordRequest(@RequestBody ForgotPassword forgotPassword) {
        return forgotPasswordFromRepository(forgotPassword);
    }


    private String forgotPasswordFromRepository(ForgotPassword forgotPassword) {
        List<Member> list = usersRepository.forgotPasswordFromDb(forgotPassword.getUserName(), forgotPassword.getEmail());
        if (!list.isEmpty()) {
            return list.get(0).getPassword();
        }
        return "User does not exists , check username and email";
    }

    private RequestConfirmation loginUserConfirmation(Login login) {
        if (!usersRepository.loginUser(login.getUserName(), login.getPassword()).isEmpty()) {
            if (!checkIfUserIsAlreadyLogged(login.getUserName())) {
                return new RequestConfirmation(true, null);
            }
            return new RequestConfirmation(false, "User is already logged");
        }
        return new RequestConfirmation(false, "Wrong username or password");
    }

    private RequestConfirmation registerUserConfirmation(Register register) {
        try {
            if (!usersRepository.nameExists(register.getUserName()).isEmpty()) {
                return new RequestConfirmation(false, "Username already exists");
            }
            if (!usersRepository.emailExists(register.getEmailAddress()).isEmpty()) {
                return new RequestConfirmation(false, "Email already exists");
            }
            Logger.getInstance().log(register.getUserName()+" was registered");
            usersRepository.addUser(register.getUserName(), register.getPassword(), register.getEmailAddress());
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
