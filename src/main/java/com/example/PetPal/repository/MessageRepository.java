package com.example.PetPal.repository;

import com.example.PetPal.model.Message;
import com.example.PetPal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.timestamp ASC")
    List<Message> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
    @Query(value = """
    SELECT DISTINCT u.* FROM users u
    WHERE u.id IN (
        SELECT m.receiver_id FROM message m WHERE m.sender_id = :userId
        UNION
        SELECT m.sender_id FROM message m WHERE m.receiver_id = :userId
    )
""", nativeQuery = true)
    List<User> findChatPartners(@Param("userId") Long userId);
}
