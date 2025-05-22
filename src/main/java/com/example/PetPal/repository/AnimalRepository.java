package com.example.PetPal.repository;

import com.example.PetPal.model.Animal;
import com.example.PetPal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByOwner(User owner);
    Optional<Animal> findByOwnerAndId(User owner, Long id);
}