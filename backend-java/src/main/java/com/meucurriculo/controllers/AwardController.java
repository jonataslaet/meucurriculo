package com.meucurriculo.controllers;

import com.meucurriculo.controllers.dtos.AwardInputDTO;
import com.meucurriculo.controllers.dtos.AwardOutputDTO;
import com.meucurriculo.services.AwardService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/awards")
public class AwardController {

    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @GetMapping
    public ResponseEntity<@NonNull List<AwardOutputDTO>> readAwards() {
        List<AwardOutputDTO> listedAwards = awardService.getAwards();
        return ResponseEntity.ok(listedAwards);
    }

    @PostMapping
    public ResponseEntity<@NonNull AwardOutputDTO> createAward(@RequestBody @Valid AwardInputDTO awardDTO) {
        AwardOutputDTO createdAward = awardService.createAward(awardDTO);
        return ResponseEntity.ok(createdAward);
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<@NonNull AwardOutputDTO> getAwardById(@PathVariable("awardId") Long awardId) {
        AwardOutputDTO foundAward = awardService.getAwardById(awardId);
        return ResponseEntity.ok(foundAward);
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<@NonNull AwardOutputDTO> updateAward(
            @PathVariable("awardId") Long awardId,
            @Valid @RequestBody AwardInputDTO awardInputDTO) {

        AwardOutputDTO updatedAward = awardService.updateAward(awardId, awardInputDTO);
        return ResponseEntity.ok(updatedAward);
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<@NonNull Void> deleteAward(@PathVariable("awardId") Long awardId) {
        awardService.deleteAward(awardId);
        return ResponseEntity.noContent().build();
    }
}
