package com.example.PetPal.controller;

import com.example.PetPal.model.Animal;
import com.example.PetPal.model.Mood;
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

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final AnimalService animalService;
    private final Random random = new Random();

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        model.addAttribute("user", currentUser);

        if (animals.isEmpty()) {
            return "redirect:/select-pet";
        }

        Animal pet = animals.get(0);

        List<Task> tasks = getTasksFromSession(session);
        if (tasks == null || tasks.isEmpty()) {
            tasks = generateDailyTasks(pet);
            session.setAttribute("dailyTasks", tasks);
        } else {
            checkOverdueTasks(tasks, pet);
        }

        model.addAttribute("pet", pet);
        model.addAttribute("tasks", tasks);

        return "dashboard";
    }

    @PostMapping("/feed")
    public String feedPet(RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.feedAnimal(animals.get(0));

            List<Task> tasks = getTasksFromSession(session);
            if (tasks != null) {
                for (Task task : tasks) {
                    if (task.getTaskType() == Task.TaskType.FEEDING && task.isInTimeWindow()) {
                        task.setActionPerformed(true);
                        break;
                    }
                }
                session.setAttribute("dailyTasks", tasks);
            }

            redirectAttributes.addAttribute("sound", "eating");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/play")
    public String playWithPet(RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.playWithAnimal(animals.get(0));

            List<Task> tasks = getTasksFromSession(session);
            if (tasks != null) {
                for (Task task : tasks) {
                    if ((task.getTaskType() == Task.TaskType.PLAYING ||
                            task.getTaskType() == Task.TaskType.WALKING) &&
                            task.isInTimeWindow()) {
                        task.setActionPerformed(true);
                        break;
                    }
                }
                session.setAttribute("dailyTasks", tasks);
            }

            redirectAttributes.addAttribute("sound", "playing");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/vet")
    public String takeToVet(RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);
        if (!animals.isEmpty()) {
            animalService.takeToVet(animals.get(0));

            List<Task> tasks = getTasksFromSession(session);
            if (tasks != null) {
                for (Task task : tasks) {
                    if ((task.getTaskType() == Task.TaskType.VET ||
                            task.getTaskType() == Task.TaskType.GROOMING) &&
                            task.isInTimeWindow()) {
                        task.setActionPerformed(true);
                        break;
                    }
                }
                session.setAttribute("dailyTasks", tasks);
            }

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
                             @RequestParam(required = true) String currentPassword,
                             @RequestParam(required = true) String newPassword,
                             RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();

        if (!userService.validateUser(currentUser.getUsername(), currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/profile";
        }

        currentUser.setUsername(username);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            userService.updatePassword(currentUser, newPassword);
        }

        redirectAttributes.addFlashAttribute("success", "Password updated successfully");
        return "redirect:/profile";
    }

    @GetMapping("/tasks")
    public String tasks(Model model, HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        model.addAttribute("user", currentUser);
        if (!animals.isEmpty()) {
            Animal pet = animals.get(0);

            List<Task> tasks = getTasksFromSession(session);
            if (tasks == null || tasks.isEmpty()) {
                tasks = generateDailyTasks(pet);
                session.setAttribute("dailyTasks", tasks);
            } else {
                checkOverdueTasks(tasks, pet);
            }

            model.addAttribute("pet", pet);
            model.addAttribute("tasks", tasks);
            model.addAttribute("currentTime", LocalDateTime.now());
        }

        return "tasks";
    }

    @PostMapping("/task/complete")
    public String completeTask(@RequestParam Long taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        if (!animals.isEmpty()) {
            Animal pet = animals.get(0);
            List<Task> tasks = getTasksFromSession(session);

            if (tasks != null) {
                for (Task task : tasks) {
                    if (task.getId() != null && task.getId().equals(taskId)) {
                        if (task.canComplete()) {
                            task.setDone(true);
                            task.setCompletedAt(LocalDateTime.now());

                            switch (task.getTaskType()) {
                                case FEEDING:
                                    pet.setHunger(Math.max(0, pet.getHunger() - 30));
                                    pet.setHappiness(Math.min(100, pet.getHappiness() + 5));
                                    redirectAttributes.addAttribute("sound", "eating");
                                    break;
                                case PLAYING:
                                    pet.setHappiness(Math.min(100, pet.getHappiness() + 15));
                                    redirectAttributes.addAttribute("sound", "playing");
                                    break;
                                case WALKING:
                                    pet.setHappiness(Math.min(100, pet.getHappiness() + 10));
                                    pet.setHealth(Math.min(100, pet.getHealth() + 5));
                                    redirectAttributes.addAttribute("sound", "playing");
                                    break;
                                case GROOMING:
                                    pet.setHealth(Math.min(100, pet.getHealth() + 10));
                                    redirectAttributes.addAttribute("sound", "playing");
                                    break;
                                case VET:
                                    pet.setHealth(Math.min(100, pet.getHealth() + 20));
                                    redirectAttributes.addAttribute("sound", "crying");
                                    break;
                            }

                            animalService.updateAnimal(pet);
                        }
                        break;
                    }
                }

                session.setAttribute("dailyTasks", tasks);
            }
        }

        return "redirect:/tasks";
    }

    @PostMapping("/tasks/reset")
    public String resetTasks(HttpSession session) {
        User currentUser = getCurrentUser();
        List<Animal> animals = animalService.getUserAnimals(currentUser);

        if (!animals.isEmpty()) {
            Animal pet = animals.get(0);
            List<Task> tasks = generateDailyTasks(pet);
            session.setAttribute("dailyTasks", tasks);
        }

        return "redirect:/tasks";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @SuppressWarnings("unchecked")
    private List<Task> getTasksFromSession(HttpSession session) {
        return (List<Task>) session.getAttribute("dailyTasks");
    }

    private void checkOverdueTasks(List<Task> tasks, Animal pet) {
        boolean needsUpdate = false;
        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {
            if (!task.isDone() && task.getEndTime() != null && now.isAfter(task.getEndTime()) &&
                    (task.getCompletedAt() == null || task.getCompletedAt().isBefore(task.getEndTime()))) {

                switch (task.getTaskType()) {
                    case FEEDING:
                        pet.setHunger(Math.min(100, pet.getHunger() + 20));
                        pet.setHappiness(Math.max(0, pet.getHappiness() - 10));
                        if (pet.getHunger() > 70) {
                            pet.setMood(Mood.HUNGRY);
                        }
                        needsUpdate = true;
                        break;
                    case PLAYING:
                        pet.setHappiness(Math.max(0, pet.getHappiness() - 15));
                        if (pet.getHappiness() < 30) {
                            pet.setMood(Mood.SAD);
                        }
                        needsUpdate = true;
                        break;
                    case WALKING:
                        pet.setHappiness(Math.max(0, pet.getHappiness() - 10));
                        pet.setHealth(Math.max(0, pet.getHealth() - 5));
                        needsUpdate = true;
                        break;
                    case GROOMING:
                        pet.setHealth(Math.max(0, pet.getHealth() - 8));
                        if (pet.getHealth() < 30) {
                            pet.setMood(Mood.SICK);
                        }
                        needsUpdate = true;
                        break;
                    case VET:
                        pet.setHealth(Math.max(0, pet.getHealth() - 15));
                        if (pet.getHealth() < 30) {
                            pet.setMood(Mood.SICK);
                        }
                        needsUpdate = true;
                        break;
                }
            }
        }

        if (needsUpdate) {
            animalService.updateAnimal(pet);
        }
    }

    private List<Task> generateDailyTasks(Animal animal) {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();


        LocalDateTime startTime = now.withMinute(0).withSecond(0);

        tasks.add(new Task("Feed " + animal.getName(), startTime, Task.TaskType.FEEDING,
                "Your pet will get hungry and unhappy if not fed on time."));

        LocalDateTime playTime = startTime.plusHours(3);
        tasks.add(new Task("Play with " + animal.getName(), playTime, Task.TaskType.PLAYING,
                "Your pet will get sad if you don't play with them."));

        LocalDateTime walkTime = startTime.plusHours(6);
        tasks.add(new Task("Walk " + animal.getName(), walkTime, Task.TaskType.WALKING,
                "Your pet needs exercise to stay healthy and happy."));

        if (random.nextInt(3) == 0) {
            LocalDateTime groomTime = startTime.plusHours(9);
            tasks.add(new Task("Groom " + animal.getName(), groomTime, Task.TaskType.GROOMING,
                    "Grooming helps keep your pet clean and healthy."));
        }

        if (random.nextInt(7) == 0) {
            LocalDateTime vetTime = startTime.plusHours(12);
            tasks.add(new Task("Take " + animal.getName() + " to the vet", vetTime, Task.TaskType.VET,
                    "Regular vet checkups are important for your pet's health."));
        }

        return tasks;
    }
}