package com.meucurriculo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate joinDate;
    private LocalDate exitDate;

    public Project() {}

    public Project(String description, LocalDate joinDate, LocalDate exitDate) {
        this.description = description;
        this.joinDate = joinDate;
        this.exitDate = exitDate;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getJoinDate() { return joinDate; }
    public LocalDate getExitDate() { return exitDate; }

    public void setDescription(String description) { this.description = description; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public void setExitDate(LocalDate exitDate) { this.exitDate = exitDate; }
}
