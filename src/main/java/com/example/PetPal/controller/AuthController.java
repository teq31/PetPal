package com.example.PetPal.controller;

import com.example.PetPal.model.User;
import com.example.PetPal.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String username,
                                @RequestParam String password,
                                Model model) {
        try {
            User user = userService.registerUser(username, password);
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        try {
            User user = userService.authenticateUser(username, password);

            if (user != null) {
                // Setează utilizatorul în sesiune
                session.setAttribute("user", user);
                System.out.println("User logged in: " + user.getUsername() + " with ID: " + user.getId());
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            model.addAttribute("error", "Login failed");
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}