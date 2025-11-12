package com.meucurriculo.mappers;

import com.meucurriculo.controllers.dtos.*;
import com.meucurriculo.entities.HardSkillProject;
import com.meucurriculo.entities.Project;
import com.meucurriculo.entities.HardSkill;
import com.meucurriculo.entities.HardSkillProjectId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardSkillProjectMapper {

    public static HardSkillProject toEntity(HardSkillProjectInputDTO dto, Project project, HardSkill hardSkill) {
        HardSkillProjectId id = new HardSkillProjectId(project.getId(), hardSkill.getId(), dto.appliedSince());
        return new HardSkillProject(id, dto.description(), dto.appliedUntil(), project, hardSkill);
    }

    public static HardSkillProjectOutputDTO toOutputDTO(HardSkillProject entity) {
        return new HardSkillProjectOutputDTO(entity.getHardSkill().getId(), entity.getDescription(), entity.getAppliedSince(), entity.getAppliedUntil());
    }

    public static HardSkillProjectListOutputDTO toListOutputDTO(HardSkillProject entity) {
        HardSkill hs = entity.getHardSkill();
    HardSkillOutputDTO hardSkillDTO = new HardSkillOutputDTO(hs.getId(), hs.getName());
    return new HardSkillProjectListOutputDTO(
        hs.getId(),
        entity.getDescription(),
        entity.getAppliedSince(),
        entity.getAppliedUntil(),
        hardSkillDTO
    );
    }
}
