package com.meucurriculo.testsupport.factories;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;

import java.time.LocalDate;

public final class ProjectFactory {
    private ProjectFactory() {}

    public static ProjectInputDTO sampleOngoingProject() {
        return new ProjectInputDTO(
                "Build portfolio site",
                LocalDate.of(2024, 1, 15),
                null
        );
    }

    public static ProjectInputDTO sampleFinishedProject() {
        return new ProjectInputDTO(
                "Contribute to OSS",
                LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 12, 31)
        );
    }

    public static ProjectInputDTO blankDescription() {
        return new ProjectInputDTO(
                "",
                LocalDate.of(2024, 1, 1),
                null
        );
    }
}
