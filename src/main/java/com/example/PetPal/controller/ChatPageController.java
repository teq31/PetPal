package com.example.PetPal.controller;

import com.example.PetPal.model.User;
import com.example.PetPal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatPageController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/chat")
    public String openChatPage(@RequestParam Long senderId,
                               @RequestParam Long receiverId,
                               Model model) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);

        if (sender == null || receiver == null) {
            model.addAttribute("error", "User not found");
            return "error"; // asigură-te că ai o pagină `error.html`
        }

        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("senderUsername", sender.getUsername());
        model.addAttribute("receiverUsername", receiver.getUsername());

        return "chat";
    }
}
