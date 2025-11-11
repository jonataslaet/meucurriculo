package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.HardSkillProjectInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectOutputDTO;
import com.meucurriculo.controllers.dtos.HardSkillProjectListOutputDTO;
import com.meucurriculo.services.HardSkillProjectService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/hardskills")
public class HardSkillProjectController {

    private final HardSkillProjectService service;

    public HardSkillProjectController(HardSkillProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<@NonNull List<HardSkillProjectListOutputDTO>> list(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(service.list(projectId));
    }

    @PostMapping
    public ResponseEntity<@NonNull HardSkillProjectOutputDTO> create(@PathVariable("projectId") Long projectId,
                                                                     @Valid @RequestBody HardSkillProjectInputDTO dto) {
        return ResponseEntity.ok(service.create(projectId, dto));
    }

    @GetMapping("/{hardSkillId}/{since}")
    public ResponseEntity<@NonNull HardSkillProjectOutputDTO> get(@PathVariable("projectId") Long projectId,
                                                                  @PathVariable("hardSkillId") Long hardSkillId,
                                                                  @PathVariable("since") java.time.LocalDate since) {
        return ResponseEntity.ok(service.getById(projectId, hardSkillId, since));
    }

    @PutMapping("/{hardSkillId}/{since}")
    public ResponseEntity<@NonNull HardSkillProjectOutputDTO> update(@PathVariable("projectId") Long projectId,
                                                                     @PathVariable("hardSkillId") Long hardSkillId,
                                                                     @PathVariable("since") java.time.LocalDate since,
                                                                     @Valid @RequestBody HardSkillProjectInputDTO dto) {
        return ResponseEntity.ok(service.update(projectId, hardSkillId, since, dto));
    }

    @DeleteMapping("/{hardSkillId}/{since}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable("projectId") Long projectId,
                                                @PathVariable("hardSkillId") Long hardSkillId,
                                                @PathVariable("since") java.time.LocalDate since) {
        service.delete(projectId, hardSkillId, since);
        return ResponseEntity.noContent().build();
    }
}
