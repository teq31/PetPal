package com.example.PetPal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String name;
    private String time;
    private boolean done;
    private int id;
}
