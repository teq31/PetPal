package com.example.PetPal.service;

import com.example.PetPal.model.Animal;
import com.example.PetPal.model.Mood;
import com.example.PetPal.model.User;
import com.example.PetPal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public Animal createAnimal(String name, String species, User owner) {
        Animal animal = new Animal();
        animal.setName(name);
        animal.setSpecies(species);
        animal.setOwner(owner);
        animal.setMood(Mood.HAPPY);
        animal.setHappiness(100);
        animal.setHealth(100);
        animal.setHunger(0);

        return animalRepository.save(animal);
    }

    public List<Animal> getUserAnimals(User user) {
        return animalRepository.findByOwner(user);
    }

    public Optional<Animal> getAnimalById(Long id, User owner) {
        return animalRepository.findByOwnerAndId(owner, id);
    }

    public Animal feedAnimal(Animal animal) {
        animal.setHunger(Math.max(0, animal.getHunger() - 30));
        animal.setHappiness(Math.min(100, animal.getHappiness() + 10));
        animal.setLastFed(LocalDateTime.now());
        updateAnimalMood(animal);
        return animalRepository.save(animal);
    }

    public Animal playWithAnimal(Animal animal) {
        animal.setHappiness(Math.min(100, animal.getHappiness() + 20));
        animal.setHunger(Math.min(100, animal.getHunger() + 10));
        animal.setLastPlayed(LocalDateTime.now());
        updateAnimalMood(animal);
        return animalRepository.save(animal);
    }

    public Animal takeToVet(Animal animal) {
        animal.setHealth(100);
        animal.setHappiness(Math.max(0, animal.getHappiness() - 5));
        updateAnimalMood(animal);
        return animalRepository.save(animal);
    }

    private void updateAnimalMood(Animal animal) {
        if (animal.getHealth() < 30) {
            animal.setMood(Mood.SICK);
        } else if (animal.getHunger() > 70) {
            animal.setMood(Mood.HUNGRY);
        } else if (animal.getHappiness() < 30) {
            animal.setMood(Mood.SAD);
        } else {
            animal.setMood(Mood.HAPPY);
        }
    }

    public Animal updateAnimal(Animal animal) {
        return animalRepository.save(animal);
    }
}