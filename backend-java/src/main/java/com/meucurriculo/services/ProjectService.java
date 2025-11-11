package com.meucurriculo.services;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;
import com.meucurriculo.controllers.dtos.ProjectOutputDTO;
import com.meucurriculo.entities.Project;
import com.meucurriculo.exceptions.ResourceNotFoundException;
import com.meucurriculo.mappers.ProjectMapper;
import com.meucurriculo.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectOutputDTO> getProjects() {
        return repository.findAllAsDTO();
    }

    @Transactional
    public ProjectOutputDTO createProject(ProjectInputDTO dto) {
        Project saved = repository.save(ProjectMapper.toEntity(dto));
        return ProjectMapper.toOutputDTO(saved);
    }

    public ProjectOutputDTO getProjectById(Long id) {
        Project found = getEntity(id);
        return ProjectMapper.toOutputDTO(found);
    }

    @Transactional
    public ProjectOutputDTO updateProject(Long id, ProjectInputDTO dto) {
        Project entity = getEntity(id);
        entity.setDescription(dto.description());
        entity.setJoinDate(dto.joinDate());
        entity.setExitDate(dto.exitDate());
        Project saved = repository.save(entity);
        return ProjectMapper.toOutputDTO(saved);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!repository.existsById(id)) {
            logger.warn("Tentativa de deletar Project inexistente. ID: {}", id);
            throw new ResourceNotFoundException("Project não encontrado");
        }
        repository.deleteById(id);
    }

    public Project getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.warn("Tentativa de acessar Project inexistente. ID: {}", id);
            return new ResourceNotFoundException("Project não encontrado");
        });
    }
}
