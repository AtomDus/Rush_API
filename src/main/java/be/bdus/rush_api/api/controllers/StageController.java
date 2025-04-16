package be.bdus.rush_api.api.controllers;


import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.stage.dtos.StageDTO;
import be.bdus.rush_api.bll.services.StageService;
import be.bdus.rush_api.dl.entities.Stage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stages")

@Tag(name = "Stages", description = "Endpoints use for managing stages")
public class StageController {

    private final StageService stageService;

    @Operation(summary = "Listing all stages", description = "Use to list all stages")
    @GetMapping
    public ResponseEntity<CustomPage<StageDTO>> getAllProject(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Stage> stages = stageService.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<StageDTO> dtos = stages.getContent().stream()
                .map(StageDTO::fromStage)
                .toList();
        CustomPage<StageDTO> result = new CustomPage<>(dtos,stages.getTotalPages(),stages.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing stages by id", description = "Let the user search an stages with its id")
    @GetMapping("/{id}")
    public ResponseEntity<StageDTO> getById(@RequestParam Long id) {
        Stage stage = stageService.findById(id);
        return ResponseEntity.ok(StageDTO.fromStage(stage));
    }

    @Operation(summary = "Adding a stage", description = "Use to add a stage")
    @PostMapping("/add")
    public ResponseEntity<StageDTO> save(@RequestBody Stage stage) {
        return ResponseEntity.ok(StageDTO.fromStage(stageService.save(stage)));
    }

    @Operation(summary = "Updating a stage", description = "Use to update a stage")
    @PutMapping("/update/{id}")
    public ResponseEntity<StageDTO> update(@RequestBody Stage stage, @PathVariable Long id) {
        return ResponseEntity.ok(StageDTO.fromStage(stageService.update(stage, id)));
    }

    @Operation(summary = "Deleting a stage", description = "Use to delete a stage")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
