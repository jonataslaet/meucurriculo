package com.meucurriculo.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;

public record HardSkillProjectInputDTO(
                @NotBlank String description,
                        @NotNull LocalDate appliedSince,
                        LocalDate appliedUntil,
                @NotNull Long hardSkillId
) {
        @AssertTrue(message = "appliedUntil deve ser nulo ou maior/igual a appliedSince")
        public boolean isValidDateRange() {
                        return appliedUntil == null || !appliedUntil.isBefore(appliedSince);
        }
}
