package com.meucurriculo.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HardSkillProjectExperienceDTO(
        Long hardSkillId, String hardSkillName, Integer experienceTimeInMonths, Integer experienceTimeInYears) {}
