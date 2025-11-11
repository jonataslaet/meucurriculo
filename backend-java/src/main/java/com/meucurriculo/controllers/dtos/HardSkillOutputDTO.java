package com.meucurriculo.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HardSkillOutputDTO(Long id, String name) {
}
