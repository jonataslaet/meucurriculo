package com.meucurriculo.testsupport.factories;

import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;

import java.time.LocalDate;

public final class HardSkillProjectFactory {
    private HardSkillProjectFactory() {}

    public static HardSkillProjectInputDTO sampleApplied(long hardSkillId) {
        return new HardSkillProjectInputDTO(
                "Apply Java expertise",
                LocalDate.of(2024, 1, 1),
                null,
                hardSkillId
        );
    }

    public static HardSkillProjectInputDTO sampleFinished(long hardSkillId) {
        return new HardSkillProjectInputDTO(
                "Refactoring sprint",
                LocalDate.of(2023, 3, 10),
                LocalDate.of(2023, 4, 30),
                hardSkillId
        );
    }

    public static HardSkillProjectInputDTO blankDescription(long hardSkillId) {
        return new HardSkillProjectInputDTO(
                "",
                LocalDate.of(2024, 5, 5),
                null,
                hardSkillId
        );
    }
}
