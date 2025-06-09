package com.example.PetPal.dto;

public class PublicProfileDTO {
    private Long userId;
    private String username;
    private String petName;
    private String species;
    private String mood;

    public PublicProfileDTO(Long userId, String username, String petName, String species, String mood) {
        this.userId = userId;
        this.username = username;
        this.petName = petName;
        this.species = species;
        this.mood = mood;
    }

    // Getteri
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPetName() { return petName; }
    public String getSpecies() { return species; }
    public String getMood() { return mood; }

    // Setteri
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPetName(String petName) { this.petName = petName; }
    public void setSpecies(String species) { this.species = species; }
    public void setMood(String mood) { this.mood = mood; }
}
