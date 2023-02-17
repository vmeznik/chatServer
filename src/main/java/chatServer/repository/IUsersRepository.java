package chatServer.repository;

import chatServer.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IUsersRepository extends JpaRepository<Member, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users (name,password,email)" +
            " VALUES (:nameReq,:passwordReq,:emailReq)", nativeQuery = true)
    void addUser(@Param("nameReq") String nameReq, @Param("passwordReq") String passwordReq,
                 @Param("emailReq") String emailReq);

    @Query(value = "SELECT * FROM users  WHERE  name = ?1", nativeQuery = true)
    List<Member> nameExists(String nameReq);

    @Query(value = "SELECT * FROM users  WHERE  email = ?1", nativeQuery = true)
    List<Member> emailExists(String emailReq);

    @Query(value = "SELECT * FROM users  WHERE  name = ?1 AND password = ?2", nativeQuery = true)
    List<Member> loginUser(String nameReq, String passwordReq);

    @Query(value = "SELECT * FROM users  WHERE  name = ?1 AND email = ?2", nativeQuery = true)
    List<Member> forgotPasswordFromDb(String nameReq, String emailReq);


}
