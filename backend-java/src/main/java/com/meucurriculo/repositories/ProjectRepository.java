package com.meucurriculo.repositories;

import com.meucurriculo.controllers.dtos.ProjectOutputDTO;
import com.meucurriculo.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT new com.meucurriculo.controllers.dtos.ProjectOutputDTO(p.id, p.description, p.joinDate, p.exitDate) FROM Project p")
    List<ProjectOutputDTO> findAllAsDTO();
}
