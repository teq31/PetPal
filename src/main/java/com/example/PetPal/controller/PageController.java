package com.example.PetPal.controller;

import com.example.PetPal.model.Animal;
import com.example.PetPal.model.Task;
import com.example.PetPal.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.example.PetPal.model.Mood.HAPPY;

@Controller
public class PageController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/select-pet")
    public String selectPet() {
        return "select-pet";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = new User("guest", "1234");
        Animal pet = new Animal("Fluffy", "dog", HAPPY);
        model.addAttribute("user", user);
        model.addAttribute("pet", pet);
        List<Task> tasks = List.of(
                new Task("Feed Fluffy", "08:00", false, 1),
                new Task("Play with Fluffy", "12:00", true, 2),
                new Task("Go to vet", "17:00", false, 3)
        );
        model.addAttribute("tasks", tasks);
        return "dashboard";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        Animal pet = new Animal("Fluffy", "dog", HAPPY);

        List<Task> tasks = List.of(
                new Task("Feed Fluffy", "08:00", false, 1),
                new Task("Play with Fluffy", "12:00", true, 2),
                new Task("Go to vet", "17:00", false, 3)
        );

        model.addAttribute("pet", pet);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        User user = new User("guest", "1234");
        Animal pet = new Animal("Fluffy", "dog", HAPPY);
        model.addAttribute("user", user);
        model.addAttribute("pet", pet);
        return "profile";
    }
}
