package com.example.PetPal.service;

import com.example.PetPal.model.Message;
import com.example.PetPal.model.User;
import com.example.PetPal.repository.MessageRepository;
import com.example.PetPal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;

    public void sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepo.findById(senderId).orElseThrow();
        User receiver = userRepo.findById(receiverId).orElseThrow();
        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        msg.setAttachment(false);
        messageRepo.save(msg);
    }

    public List<Message> getConversation(Long user1Id, Long user2Id) {
        User u1 = userRepo.findById(user1Id).orElseThrow();
        User u2 = userRepo.findById(user2Id).orElseThrow();
        return messageRepo.findConversationBetweenUsers(u1, u2);
    }


    public List<User> getChatPartners(Long userId) {
        return messageRepo.findChatPartners(userId);
    }


}
