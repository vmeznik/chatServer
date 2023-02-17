package chatServer.repository;

import chatServer.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface IMessagesRepository extends JpaRepository<Message, Integer> {

    @Query(value = "SELECT * FROM messages WHERE ( sender = ?1 AND receiver = ?2 ) OR" +
            " ( sender = ?2 AND receiver = ?1 )", nativeQuery = true)
    List<Message> getConversation(String sender, String receiver);

    @Query(value = "SELECT * FROM messages WHERE receiver = ?1 ", nativeQuery = true)
    List<Message> getAllChatConversation(String receiver);

}
