package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.project.dtos.ProjectDTO;
import be.bdus.rush_api.bll.services.ProjectService;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.Project;
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
@RequestMapping("/projects")

@Tag(name = "project", description = "Endpoints use for projects")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Listing all projects", description = "Use to list all projects")
    @GetMapping
    public ResponseEntity<CustomPage<ProjectDTO>> getAllProject(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Project> projects = projectService.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<ProjectDTO> dtos = projects.getContent().stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos,projects.getTotalPages(),projects.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing projects by id", description = "Let the user search an projects with its id")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@RequestParam Long id) {
        Project Project = projectService.findById(id);
        return ResponseEntity.ok(ProjectDTO.fromProject(Project));
    }

    @Operation(summary = "Adding a project", description = "Use to add a project")
    @PostMapping("/add")
    public ResponseEntity<ProjectDTO> save(@RequestBody Project project) {
        return ResponseEntity.ok(ProjectDTO.fromProject(projectService.save(project)));
    }

    @Operation(summary = "Updating a project", description = "Use to update a project")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = projectService.update(project, id);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(summary = "Deleting a project", description = "Use to delete a project")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
