package chatServer.service;

import chatServer.model.Message;
import chatServer.repository.IMessagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final IMessagesRepository messagesRepository;
    private static MessageService messageService;

    public MessageService(IMessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
        messageService = this;
    }

    public static MessageService getMessageService() {
        if (messageService != null) {
            return messageService;
        } else
            return null;
    }

    public void addMessage(Message messageToAdd) {
        messagesRepository.save(messageToAdd);
    }

    public List<Message> getConversation(String senderReq, String receiverReq) {
        if (receiverReq.equals("all")) {
            return messagesRepository.getAllChatConversation(receiverReq);
        }
        return messagesRepository.getConversation(senderReq, receiverReq);


    }

}
