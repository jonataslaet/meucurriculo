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

    public static List<HardSkillProjectExperienceDTO> toOutputDTO(List<HardSkillProject> hardSkills) {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Long> map2 = new HashMap<>();
        for (HardSkillProject hardSkillProject: hardSkills) {
            String hName = hardSkillProject.getHardSkill().getName();
            if (!map.containsKey(hName)) {
                map.put(hName, 0);
                map2.put(hName, hardSkillProject.getHardSkill().getId());
            }
            LocalDate appliedSinceDate = hardSkillProject.getId().getAppliedSince();
            LocalDate appliedUntilDate = hardSkillProject.getAppliedUntil() == null ?
                    LocalDate.now() : hardSkillProject.getAppliedUntil();
            int difference = (int) (ChronoUnit.MONTHS.between(appliedSinceDate, appliedUntilDate));
            map.put(hName, map.getOrDefault(hName, 0) + difference);
        }
        List<HardSkillProjectExperienceDTO> list = new ArrayList<>();
        for (String hardSkillName: map.keySet()) {
            Integer a = map.get(hardSkillName) > 12 ? Math.ceilDiv(map.get(hardSkillName), 12) : null;
            Integer b = a != null ? null : map.get(hardSkillName);
            list.add(new HardSkillProjectExperienceDTO(map2.get(hardSkillName), hardSkillName, b, a));
        }
        return list;

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
