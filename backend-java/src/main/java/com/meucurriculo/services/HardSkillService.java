package com.meucurriculo.services;

import com.meucurriculo.controllers.dtos.HardSkillInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillOutputDTO;
import com.meucurriculo.entities.HardSkill;
import com.meucurriculo.exceptions.ResourceNotFoundException;
import com.meucurriculo.mappers.HardSkillMapper;
import com.meucurriculo.repositories.HardSkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HardSkillService {

    private static final Logger logger = LoggerFactory.getLogger(HardSkillService.class);
    private final HardSkillRepository repository;

    public HardSkillService(HardSkillRepository repository) {
        this.repository = repository;
    }

    public List<HardSkillOutputDTO> getHardSkills() {
        return repository.findAllAsDTO();
    }

    @Transactional
    public HardSkillOutputDTO createHardSkill(HardSkillInputDTO dto) {
        HardSkill saved = repository.save(HardSkillMapper.toEntity(dto));
        return HardSkillMapper.toOutputDTO(saved);
    }

    public HardSkillOutputDTO getHardSkillById(Long id) {
        HardSkill found = getEntity(id);
        return HardSkillMapper.toOutputDTO(found);
    }

    @Transactional
    public HardSkillOutputDTO updateHardSkill(Long id, HardSkillInputDTO dto) {
        HardSkill entity = getEntity(id);
        entity.setName(dto.name());
        HardSkill saved = repository.save(entity);
        return HardSkillMapper.toOutputDTO(saved);
    }

    @Transactional
    public void deleteHardSkill(Long id) {
        if (!repository.existsById(id)) {
            logger.warn("Tentativa de deletar HardSkill inexistente. ID: {}", id);
            throw new ResourceNotFoundException("HardSkill não encontrada");
        }
        repository.deleteById(id);
    }

    public HardSkill getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.warn("Tentativa de acessar HardSkill inexistente. ID: {}", id);
            return new ResourceNotFoundException("HardSkill não encontrada");
        });
    }
}
