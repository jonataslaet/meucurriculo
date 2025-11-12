package com.meucurriculo.entities;

import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.mappers.HardSkillProjectMapper;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate joinDate;
    private LocalDate exitDate;

    @Transient
    @OneToMany(fetch = FetchType.LAZY)
    private List<HardSkillProject> hardSkillsProjects;

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

    public void addHardSkill(HardSkill hardSkill, String description, LocalDate joinDate, LocalDate exitDate) {

        HardSkillProjectInputDTO hardSkillProjectInputDTO =
            new HardSkillProjectInputDTO(description, joinDate, exitDate, hardSkill.getId());

        HardSkillProject hardSkillProject =
            HardSkillProjectMapper.toEntity(hardSkillProjectInputDTO, this, hardSkill);

        if (Objects.isNull(this.hardSkillsProjects)) this.hardSkillsProjects = new ArrayList<>();
        this.hardSkillsProjects.add(hardSkillProject);
    }

    public List<HardSkillProject> getAllHardSkillProjects() {
        return this.hardSkillsProjects;
    }
}
