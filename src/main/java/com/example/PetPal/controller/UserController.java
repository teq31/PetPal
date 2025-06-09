package com.example.PetPal.controller;

import com.example.PetPal.dto.PublicProfileDTO;
import com.example.PetPal.model.User;
import com.example.PetPal.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔹 Pagina HTML pentru explore
    @GetMapping("/explore")
    public String explorePage(HttpSession session, Model model) {
        System.out.println("Current user in session: " + session.getAttribute("user"));

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<PublicProfileDTO> profiles = userService.getAllPublicProfiles().stream()
                .filter(p -> !p.getUserId().equals(currentUser.getId())) // optional: excludem utilizatorul curent
                .toList();

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("profiles", profiles);
        return "explore";
    }



    // 🔹 API REST care returnează lista de profile publice
    @GetMapping("/api/users/all")
    @ResponseBody
    public List<PublicProfileDTO> getAllProfiles() {
        return userService.getAllPublicProfiles();
    }
}
