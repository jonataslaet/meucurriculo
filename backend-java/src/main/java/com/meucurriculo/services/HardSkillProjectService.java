package com.meucurriculo.services;

import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectOutputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectListOutputDTO;
import com.meucurriculo.entities.HardSkillProject;
import com.meucurriculo.entities.HardSkillProjectId;
import com.meucurriculo.entities.HardSkill;
import com.meucurriculo.entities.Project;
import com.meucurriculo.exceptions.ResourceNotFoundException;
import com.meucurriculo.mappers.HardSkillProjectMapper;
import com.meucurriculo.repositories.HardSkillProjectRepository;
import com.meucurriculo.repositories.ProjectRepository;
import com.meucurriculo.repositories.HardSkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HardSkillProjectService {

    private static final Logger logger = LoggerFactory.getLogger(HardSkillProjectService.class);
    private final HardSkillProjectRepository hardSkillProjectRepository;
    private final ProjectRepository projectRepository;
    private final HardSkillRepository hardSkillRepository;

    public HardSkillProjectService(HardSkillProjectRepository hardSkillProjectRepository,
                                   ProjectRepository projectRepository, HardSkillRepository hardSkillRepository) {
        this.hardSkillProjectRepository = hardSkillProjectRepository;
        this.projectRepository = projectRepository;
        this.hardSkillRepository = hardSkillRepository;
    }

    private void ensureProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            logger.warn("Tentativa de acessar HardSkill de projeto inexistente. Project ID: {}", projectId);
            throw new ResourceNotFoundException("Projeto não encontrado");
        }
    }

    public List<HardSkillProjectListOutputDTO> list(Long projectId) {
        ensureProjectExists(projectId);
        return hardSkillProjectRepository.findAllByProjectId(projectId)
                .stream()
                .map(HardSkillProjectMapper::toListOutputDTO)
                .toList();
    }

    @Transactional
    public HardSkillProjectOutputDTO create(Long projectId, HardSkillProjectInputDTO dto) {
        ensureProjectExists(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Projeto não encontrado"));
        HardSkill hardSkill = hardSkillRepository.findById(dto.hardSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("HardSkill não encontrada"));
        HardSkillProject saved = hardSkillProjectRepository.save(HardSkillProjectMapper.toEntity(dto, project, hardSkill));
        return HardSkillProjectMapper.toOutputDTO(saved);
    }

    public HardSkillProjectOutputDTO getById(Long projectId, Long hardSkillId, LocalDate since) {
        ensureProjectExists(projectId);
        HardSkillProjectId id = new HardSkillProjectId(projectId, hardSkillId, since);
        HardSkillProject entity = hardSkillProjectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("HardSkillProject não encontrado"));
        return HardSkillProjectMapper.toOutputDTO(entity);
    }

    @Transactional
    public HardSkillProjectOutputDTO update(Long projectId, Long hardSkillId, LocalDate since, HardSkillProjectInputDTO dto) {
        ensureProjectExists(projectId);
        HardSkillProjectId oldId = new HardSkillProjectId(projectId, hardSkillId, since);
        HardSkillProject existing = hardSkillProjectRepository.findById(oldId).orElseThrow(() ->
                new ResourceNotFoundException("HardSkillProject não encontrado"));

        boolean keyChanged = !dto.hardSkillId().equals(hardSkillId) || !dto.appliedSince().equals(since);
        Project project = existing.getProject();
        HardSkill newHardSkill = hardSkillRepository.findById(dto.hardSkillId()).orElseThrow(() ->
                new ResourceNotFoundException("HardSkill não encontrada"));

        if (keyChanged) {
            HardSkillProject recreated = HardSkillProjectMapper.toEntity(dto, project, newHardSkill);
            HardSkillProject saved = hardSkillProjectRepository.save(recreated);
            hardSkillProjectRepository.delete(existing);
            return HardSkillProjectMapper.toOutputDTO(saved);
        } else {
            existing.setDescription(dto.description());
            existing.setHardSkill(newHardSkill);
            existing.setAppliedUntil(dto.appliedUntil());
            return HardSkillProjectMapper.toOutputDTO(hardSkillProjectRepository.save(existing));
        }
    }

    @Transactional
    public void delete(Long projectId, Long hardSkillId, LocalDate since) {
        ensureProjectExists(projectId);
        HardSkillProjectId id = new HardSkillProjectId(projectId, hardSkillId, since);
        HardSkillProject entity = hardSkillProjectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("HardSkillProject não encontrado"));
        hardSkillProjectRepository.delete(entity);
    }
}
