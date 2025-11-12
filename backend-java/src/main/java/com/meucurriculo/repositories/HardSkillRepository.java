package com.meucurriculo.repositories;

import com.meucurriculo.controllers.dtos.HardSkillOutputDTO;
import com.meucurriculo.entities.HardSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardSkillRepository extends JpaRepository<HardSkill, Long> {

    @Query("SELECT new com.meucurriculo.controllers.dtos.HardSkillOutputDTO(h.id, h.name) FROM HardSkill h")
    List<HardSkillOutputDTO> findAllAsDTO();

    @Query("SELECT h FROM HardSkill h WHERE h.name like :name")
    HardSkill findByName(@Param("name") String name);
}
