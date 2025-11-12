package com.meucurriculo.repositories;

import com.meucurriculo.entities.HardSkillProject;
import com.meucurriculo.entities.HardSkillProjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface HardSkillProjectRepository extends JpaRepository<HardSkillProject, HardSkillProjectId> {

    @Query("SELECT h FROM HardSkillProject h JOIN FETCH h.hardSkill WHERE h.project.id = :projectId")
    List<HardSkillProject> findAllByProjectId(@Param("projectId") Long projectId);

    @Query(
        value = """
            SELECT new com.meucurriculo.controllers.dtos.HardSkillProjectExperienceDTO(
                hs.name,
                SUM( (YEAR(COALESCE(h.appliedUntil, CURRENT_DATE)) * 12 + MONTH(COALESCE(h.appliedUntil, CURRENT_DATE)))
                    - (YEAR(h.id.appliedSince) * 12 + MONTH(h.id.appliedSince)) ),
                SUM( YEAR(COALESCE(h.appliedUntil, CURRENT_DATE)) - YEAR(h.id.appliedSince) )
            )
            FROM HardSkill hs
            JOIN hs.hardSkillProjects h
            WHERE LOWER(hs.name) LIKE LOWER(CONCAT(:name, '%'))
            GROUP BY hs.name
        """,
        countQuery = """
            SELECT COUNT(DISTINCT hs.id)
            FROM HardSkill hs
            JOIN hs.hardSkillProjects h
            WHERE LOWER(hs.name) LIKE LOWER(CONCAT(:name, '%'))
        """
    )
    Page<com.meucurriculo.controllers.dtos.HardSkillProjectExperienceDTO>
    findProjectExperiencesStartingWith(@Param("name") String name, Pageable pageable);


}
