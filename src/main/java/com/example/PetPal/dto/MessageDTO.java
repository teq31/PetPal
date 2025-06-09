package com.example.PetPal.dto;

import com.example.PetPal.model.Message;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String content;
    private Long senderId;
    private String senderUsername;
    private LocalDateTime timestamp;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.senderId = message.getSender().getId();
        this.senderUsername = message.getSender().getUsername();
        this.timestamp = message.getTimestamp();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public Long getSenderId() { return senderId; }
    public String getSenderUsername() { return senderUsername; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
