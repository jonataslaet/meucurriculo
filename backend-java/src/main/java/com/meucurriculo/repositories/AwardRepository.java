package com.meucurriculo.repositories;

import com.meucurriculo.controllers.dtos.AwardOutputDTO;
import com.meucurriculo.entities.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("SELECT new com.meucurriculo.controllers.dtos.AwardOutputDTO(a.id, a.title, a.description, a.year) FROM Award a")
    List<AwardOutputDTO> findAllAsDTO();
}
