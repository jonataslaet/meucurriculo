package com.meucurriculo.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AwardInputDTO(@NotBlank String title, @NotBlank String description, @NotNull int year) {
}