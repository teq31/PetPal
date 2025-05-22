package com.example.PetPal.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Task {
    private String name;
    private String time;
    private boolean done;
    private int id;

    public Task(String name, String time) {
        this.name = name;
        this.time = time;
        this.done = false;
        this.id = 0;
    }

    public Task(String name, String time, boolean done, int id) {
        this.name = name;
        this.time = time;
        this.done = done;
        this.id = id;
    }
}