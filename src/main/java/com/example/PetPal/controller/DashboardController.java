package com.example.PetPal.controller;

import com.example.PetPal.model.Animal;
import com.example.PetPal.model.Task;
import com.example.PetPal.model.User;
import com.example.PetPal.service.AnimalService;
import com.example.PetPal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final AnimalService animalService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        model.addAttribute("user", currentUser);

        if (animals.isEmpty()) {
            return "redirect:/select-pet";
        }

        Animal pet = animals.get(0);
        model.addAttribute("pet", pet);
        model.addAttribute("tasks", generateDailyTasks(pet));

        return "dashboard";
    }

    @PostMapping("/feed")
    public String feedPet(RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.feedAnimal(animals.get(0));
            redirectAttributes.addAttribute("sound", "eating");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/play")
    public String playWithPet(RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.playWithAnimal(animals.get(0));
            redirectAttributes.addAttribute("sound", "playing");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/vet")
    public String takeToVet(RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.takeToVet(animals.get(0));
            redirectAttributes.addAttribute("sound", "crying");
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/select-pet")
    public String selectPet() {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        if (!animals.isEmpty()) {
            return "redirect:/dashboard";
        }

        return "select-pet";
    }

    @PostMapping("/select-pet")
    public String processSelectPet(@RequestParam String species,
                                   @RequestParam String name) {
        User currentUser = getCurrentUser();
        animalService.createAnimal(name, species, currentUser);
        return "redirect:/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        model.addAttribute("user", currentUser);
        if (!animals.isEmpty()) {
            model.addAttribute("pet", animals.get(0));
        }

        return "profile";
    }

    @PostMapping("/profile/update-pet")
    public String updatePet(@RequestParam String name,
                            @RequestParam String species) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        if (!animals.isEmpty()) {
            Animal pet = animals.get(0);
            pet.setName(name);
            pet.setSpecies(species);
            animalService.updateAnimal(pet);
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/update-user")
    public String updateUser(@RequestParam String username,
                             @RequestParam(required = false) String password) {
        User currentUser = getCurrentUser();
        currentUser.setUsername(username);

        if (password != null && !password.trim().isEmpty()) {
            currentUser.setPassword(password);
        }

        userService.updateUser(currentUser);
        return "redirect:/profile";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        model.addAttribute("user", currentUser);
        if (!animals.isEmpty()) {
            Animal pet = animals.get(0);
            model.addAttribute("pet", pet);
            model.addAttribute("tasks", generateDailyTasks(pet));
        }

        return "tasks";
    }

    @PostMapping("/task/check")
    public String checkTask(@RequestParam int id) {
        return "redirect:/tasks";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private List<Task> generateDailyTasks(Animal animal) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Feed " + animal.getName(), "08:00"));
        tasks.add(new Task("Play with " + animal.getName(), "12:00"));
        tasks.add(new Task("Walk " + animal.getName(), "18:00"));
        return tasks;
    }
}