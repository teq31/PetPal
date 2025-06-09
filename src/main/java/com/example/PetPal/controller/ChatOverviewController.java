package com.example.PetPal.controller;

import com.example.PetPal.model.User;
import com.example.PetPal.service.MessageService;
import com.example.PetPal.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class ChatOverviewController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("/chat_overview")
    public String showChatOverview(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }

        Long senderId = currentUser.getId();
        List<User> chatPartners = messageService.getChatPartners(senderId);

        model.addAttribute("senderId", senderId);
        model.addAttribute("chatPartners", chatPartners);
        return "chat_overview";
    }
}
