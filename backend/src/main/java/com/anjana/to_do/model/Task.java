package com.anjana.to_do.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority; // LOW, MEDIUM, HIGH

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    private boolean completed = false;
}

enum Priority {
    LOW, MEDIUM, HIGH
}