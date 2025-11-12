package com.meucurriculo.repositories;

import com.meucurriculo.entities.HardSkillProject;
import com.meucurriculo.entities.HardSkillProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface HardSkillProjectRepository extends JpaRepository<HardSkillProject, HardSkillProjectId> {

    @Query("SELECT h FROM HardSkillProject h JOIN FETCH h.hardSkill WHERE h.project.id = :projectId")
    List<HardSkillProject> findAllByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT h FROM HardSkillProject h JOIN FETCH h.hardSkill hs")
    List<HardSkillProject> findAll();
}
