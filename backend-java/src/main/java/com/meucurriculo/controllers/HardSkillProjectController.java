package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.HardSkillProjectExperienceDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectOutputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectListOutputDTO;
import com.meucurriculo.services.HardSkillProjectService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class HardSkillProjectController {

    private final HardSkillProjectService hardSkillProjectService;

    public HardSkillProjectController(HardSkillProjectService hardSkillProjectService) {
        this.hardSkillProjectService = hardSkillProjectService;
    }

    @GetMapping("/{projectId}/hardskills")
    public ResponseEntity<@NonNull List<HardSkillProjectListOutputDTO>> list(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(hardSkillProjectService.list(projectId));
    }

    @PostMapping("/{projectId}/hardskills")
    public ResponseEntity<@NonNull HardSkillProjectOutputDTO> addHardSkillExperience(
            @PathVariable("projectId") Long projectId, @Valid @RequestBody HardSkillProjectInputDTO dto) {
        return ResponseEntity.ok(hardSkillProjectService.addHardSkillExperience(projectId, dto));
    }

    @PutMapping("/{projectId}/hardskills/{hardSkillId}/{since}")
    public ResponseEntity<@NonNull HardSkillProjectOutputDTO> updateHardSkillExperience(
            @PathVariable("projectId") Long projectId, @PathVariable("hardSkillId") Long hardSkillId,
            @PathVariable("since") java.time.LocalDate since, @Valid @RequestBody HardSkillProjectInputDTO dto) {
        return ResponseEntity.ok(hardSkillProjectService.updateHardSkillExperience(projectId, hardSkillId, since, dto));
    }

    @DeleteMapping("/{projectId}/hardskills/{hardSkillId}/{since}")
    public ResponseEntity<@NonNull Void> deleteHardSkillExperience(@PathVariable("projectId") Long projectId,
        @PathVariable("hardSkillId") Long hardSkillId, @PathVariable("since") java.time.LocalDate since) {
        hardSkillProjectService.deleteHardSkillExperience(projectId, hardSkillId, since);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hardskills")
    public ResponseEntity<Page<HardSkillProjectExperienceDTO>> getAllPagedHardSkillExperiences(
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<HardSkillProjectExperienceDTO> pagedHardSkillExperiences =
                hardSkillProjectService.getAllPagedProjectExperiences(name, pageable);
        return ResponseEntity.ok(pagedHardSkillExperiences);
    }
}
