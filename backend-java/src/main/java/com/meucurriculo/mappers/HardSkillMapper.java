package com.meucurriculo.mappers;

import com.meucurriculo.controllers.dtos.HardSkillInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillOutputDTO;
import com.meucurriculo.entities.HardSkill;

public class HardSkillMapper {

    public static HardSkill toEntity(HardSkillInputDTO dto) {
        return new HardSkill(dto.name());
    }

    public static HardSkillOutputDTO toOutputDTO(HardSkill entity) {
        return new HardSkillOutputDTO(entity.getId(), entity.getName());
    }
}
