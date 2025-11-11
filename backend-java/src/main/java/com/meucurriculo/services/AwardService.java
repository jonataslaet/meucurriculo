package com.meucurriculo.services;

import com.meucurriculo.controllers.dtos.AwardInputDTO;
import com.meucurriculo.controllers.dtos.AwardOutputDTO;
import com.meucurriculo.entities.Award;
import com.meucurriculo.exceptions.ResourceNotFoundException;
import com.meucurriculo.mappers.AwardMapper;
import com.meucurriculo.repositories.AwardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AwardService {

    private static final Logger logger = LoggerFactory.getLogger(AwardService.class);

    private final AwardRepository awardRepository;

    public AwardService(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }

    public List<AwardOutputDTO> getAwards() {
        return awardRepository.findAllAsDTO();
    }

    @Transactional
    public AwardOutputDTO createAward(AwardInputDTO awardDTO) {
        Award awardSaved = awardRepository.save(AwardMapper.toEntity(awardDTO));
        return AwardMapper.toOutputDTO(awardSaved);
    }

    public AwardOutputDTO getAwardById(Long marketId) {
        Award market = getAwardEntity(marketId);
        return AwardMapper.toOutputDTO(market);
    }

    @Transactional
    public AwardOutputDTO updateAward(Long AwardId, AwardInputDTO awardInputDTO) {
        Award awardEntity = getAwardEntity(AwardId);
        awardEntity.setTitle(awardInputDTO.title());
        awardEntity.setDescription(awardInputDTO.description());
        awardEntity.setYear(awardInputDTO.year());

        Award savedAward = awardRepository.save(awardEntity);
        return AwardMapper.toOutputDTO(savedAward);
    }

    @Transactional
    public void deleteAward(Long AwardId) {
        if (!awardRepository.existsById(AwardId)) {
            logger.warn("Tentativa de deletar premiação inexistente. ID: {}", AwardId);
            throw new ResourceNotFoundException("Premiação não encontrada");
        }
        awardRepository.deleteById(AwardId);
    }

    public Award getAwardEntity(Long AwardId) {
        return awardRepository.findById(AwardId).orElseThrow(() -> {
            logger.warn("Tentativa de encontrar premiação inexistente. ID: {}", AwardId);
            return new ResourceNotFoundException("Premiação não encontrada");
        });
    }
}
