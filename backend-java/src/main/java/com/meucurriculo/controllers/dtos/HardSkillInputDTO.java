package com.meucurriculo.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record HardSkillInputDTO(@NotBlank String name) {
}
