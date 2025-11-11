package com.meucurriculo.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hard_skill_project")
public class HardSkillProject {

    @EmbeddedId
    private HardSkillProjectId id;

    private String description;

    @MapsId("projectId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @MapsId("hardSkillId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hard_skill_id", nullable = false)
    private HardSkill hardSkill;

    public HardSkillProject() {}

    @Column(name = "applied_until")
    private LocalDate appliedUntil;

    public HardSkillProject(HardSkillProjectId id, String description, LocalDate appliedUntil, Project project, HardSkill hardSkill) {
        this.id = id;
        this.description = description;
        this.appliedUntil = appliedUntil;
        this.project = project;
        this.hardSkill = hardSkill;
    }

    public HardSkillProjectId getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getAppliedSince() { return id != null ? id.getAppliedSince() : null; }
    public LocalDate getAppliedUntil() { return appliedUntil; }

    public void setDescription(String description) { this.description = description; }
    public void setAppliedSince(LocalDate appliedSince) { if (id != null) id.setAppliedSince(appliedSince); }
    public void setAppliedUntil(LocalDate appliedUntil) { this.appliedUntil = appliedUntil; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public HardSkill getHardSkill() { return hardSkill; }
    public void setHardSkill(HardSkill hardSkill) { this.hardSkill = hardSkill; }
}
