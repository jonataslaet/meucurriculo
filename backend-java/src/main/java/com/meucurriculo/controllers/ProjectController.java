package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.ProjectInputDTO;
import com.meucurriculo.controllers.dtos.ProjectOutputDTO;
import com.meucurriculo.services.ProjectService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<@NonNull List<ProjectOutputDTO>> readProjects() {
        return ResponseEntity.ok(service.getProjects());
    }

    @PostMapping
    public ResponseEntity<@NonNull ProjectOutputDTO> createProject(@RequestBody @Valid ProjectInputDTO dto) {
        return ResponseEntity.ok(service.createProject(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ProjectOutputDTO> getProject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull ProjectOutputDTO> updateProject(@PathVariable("id") Long id, @Valid @RequestBody ProjectInputDTO dto) {
        return ResponseEntity.ok(service.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> deleteProject(@PathVariable("id") Long id) {
        service.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
