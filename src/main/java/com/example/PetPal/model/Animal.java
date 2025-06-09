package com.example.PetPal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "owner")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Enumerated(EnumType.STRING)
    private Mood mood = Mood.HAPPY;

    private int happiness = 100;
    private int health = 100;
    private int hunger = 0;

    @Column(name = "last_fed")
    private LocalDateTime lastFed;

    @Column(name = "last_played")
    private LocalDateTime lastPlayed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    public Animal(String name, String species, Mood mood) {
        this.name = name;
        this.species = species;
        this.mood = mood;
        this.happiness = 100;
        this.health = 100;
        this.hunger = 0;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}