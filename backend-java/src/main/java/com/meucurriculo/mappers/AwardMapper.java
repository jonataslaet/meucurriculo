package com.meucurriculo.mappers;

import com.meucurriculo.controllers.dtos.AwardInputDTO;
import com.meucurriculo.controllers.dtos.AwardOutputDTO;
import com.meucurriculo.entities.Award;

public class AwardMapper {

    public static Award toEntity(AwardInputDTO awardInputDTO) {
        return new Award(awardInputDTO.title(), awardInputDTO.description(), awardInputDTO.year());
    }

    public static AwardOutputDTO toOutputDTO(Award award) {
        return new AwardOutputDTO(award.getId(), award.getTitle(), award.getDescription(), award.getYear());
    }
}
