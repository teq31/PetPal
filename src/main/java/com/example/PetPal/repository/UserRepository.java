package com.example.PetPal.repository;

import com.example.PetPal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findAll();
    @Query("SELECT DISTINCT u FROM User u WHERE u.id IN " +
            "(SELECT m.sender.id FROM Message m WHERE m.receiver = :user OR m.sender = :user " +
            "UNION " +
            "SELECT m.receiver.id FROM Message m WHERE m.receiver = :user OR m.sender = :user)")
    List<User> findChatPartners(@Param("user") User user);


}