package com.meucurriculo.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class HardSkillProjectId implements Serializable {
    private Long projectId;
    private Long hardSkillId;
    private LocalDate appliedSince;

    public HardSkillProjectId() {}

    public HardSkillProjectId(Long projectId, Long hardSkillId, LocalDate appliedSince) {
        this.projectId = projectId;
        this.hardSkillId = hardSkillId;
        this.appliedSince = appliedSince;
    }

    public Long getProjectId() { return projectId; }
    public Long getHardSkillId() { return hardSkillId; }
    public LocalDate getAppliedSince() { return appliedSince; }

    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setHardSkillId(Long hardSkillId) { this.hardSkillId = hardSkillId; }
    public void setAppliedSince(LocalDate appliedSince) { this.appliedSince = appliedSince; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HardSkillProjectId that)) return false;
    return Objects.equals(projectId, that.projectId)
        && Objects.equals(hardSkillId, that.hardSkillId)
        && Objects.equals(appliedSince, that.appliedSince);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, hardSkillId, appliedSince);
    }
}
