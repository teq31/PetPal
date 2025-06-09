package com.example.PetPal.service;

import com.example.PetPal.dto.PublicProfileDTO;
import com.example.PetPal.model.User;
import com.example.PetPal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<PublicProfileDTO> getAllPublicProfiles() {
        return userRepository.findAll().stream()
                .filter(u -> u.getAnimal() != null)
                .map(u -> new PublicProfileDTO(
                        u.getId(),
                        u.getUsername(),
                        u.getAnimal().getName(),
                        u.getAnimal().getSpecies(),
                        u.getAnimal().getMood() != null ? u.getAnimal().getMood().toString() : "UNKNOWN"
                ))
                .toList();
    }

    public User authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
