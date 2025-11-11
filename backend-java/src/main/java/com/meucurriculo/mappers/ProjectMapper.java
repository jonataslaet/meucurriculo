package com.meucurriculo.mappers;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;
import com.meucurriculo.controllers.dtos.ProjectOutputDTO;
import com.meucurriculo.entities.Project;

public class ProjectMapper {
    public static Project toEntity(ProjectInputDTO dto) {
        return new Project(dto.description(), dto.joinDate(), dto.exitDate());
    }

    public static ProjectOutputDTO toOutputDTO(Project entity) {
        return new ProjectOutputDTO(entity.getId(), entity.getDescription(), entity.getJoinDate(), entity.getExitDate());
    }
}
