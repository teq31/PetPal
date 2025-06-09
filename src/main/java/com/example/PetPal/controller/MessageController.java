package com.example.PetPal.controller;

import com.example.PetPal.dto.MessageDTO;
import com.example.PetPal.model.Message;
import com.example.PetPal.model.User;
import com.example.PetPal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public void sendMessage(@RequestParam Long senderId,
                            @RequestParam Long receiverId,
                            @RequestParam String content) {
        messageService.sendMessage(senderId, receiverId, content);
    }

    @GetMapping("/conversation")
    public List<MessageDTO> getMessages(@RequestParam Long user1Id,
                                        @RequestParam Long user2Id) {
        List<Message> messages = messageService.getConversation(user1Id, user2Id);
        return messages.stream().map(MessageDTO::new).toList();
    }

    @GetMapping("/contacts")
    public List<User> getChatPartners(@RequestParam Long userId) {
        return messageService.getChatPartners(userId);
    }


}
