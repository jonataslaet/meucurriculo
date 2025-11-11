package com.meucurriculo.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HardSkillProjectListOutputDTO(
        Long hardSkillId,
        String description,
        LocalDate appliedSince,
        LocalDate appliedUntil,
        HardSkillOutputDTO hardSkill
) {}
