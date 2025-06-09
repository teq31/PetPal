package com.example.PetPal.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class Task {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean done;
    private String effect;
    private LocalDateTime completedAt;
    private TaskType taskType;
    private boolean actionPerformed;

    public enum TaskType {
        FEEDING, PLAYING, WALKING, GROOMING, VET
    }

    public String getFormattedTimeRange() {
        if (startTime == null || endTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(formatter) + " - " + endTime.format(formatter);
    }

    public String getFormattedTime() {
        if (startTime == null) return "";
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public boolean isPastDue() {
        return endTime != null && LocalDateTime.now().isAfter(endTime) && !done;
    }

    public boolean isInTimeWindow() {
        LocalDateTime now = LocalDateTime.now();
        return startTime != null && endTime != null &&
                now.isAfter(startTime) && now.isBefore(endTime);
    }

    public boolean canComplete() {
        return isInTimeWindow() && actionPerformed && !done;
    }

    public Task(String name, LocalDateTime startTime, TaskType taskType, String effect) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = startTime.plusHours(3); // Interval de 3 ore
        this.done = false;
        this.taskType = taskType;
        this.effect = effect;
        this.id = System.currentTimeMillis();
        this.actionPerformed = false;
    }
}