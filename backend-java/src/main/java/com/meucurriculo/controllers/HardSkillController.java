package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.HardSkillInputDTO;
import com.meucurriculo.controllers.dtos.HardSkillOutputDTO;
import com.meucurriculo.services.HardSkillService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hardskills")
public class HardSkillController {

    private final HardSkillService service;

    public HardSkillController(HardSkillService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<@NonNull List<HardSkillOutputDTO>> readHardSkills() {
        List<HardSkillOutputDTO> all = service.getHardSkills();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<@NonNull HardSkillOutputDTO> createHardSkill(@RequestBody @Valid HardSkillInputDTO dto) {
        HardSkillOutputDTO created = service.createHardSkill(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull HardSkillOutputDTO> getHardSkillById(@PathVariable("id") Long id) {
        HardSkillOutputDTO found = service.getHardSkillById(id);
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull HardSkillOutputDTO> updateHardSkill(
            @PathVariable("id") Long id,
            @Valid @RequestBody HardSkillInputDTO dto) {

        HardSkillOutputDTO updated = service.updateHardSkill(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> deleteHardSkill(@PathVariable("id") Long id) {
        service.deleteHardSkill(id);
        return ResponseEntity.noContent().build();
    }
}
