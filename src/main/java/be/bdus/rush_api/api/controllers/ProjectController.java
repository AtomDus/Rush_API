package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.project.dtos.ProjectDTO;
import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.project.forms.ProjectForm;
import be.bdus.rush_api.api.models.stage.dtos.StageDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.bll.services.ProjectService;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")

@CrossOrigin("*")
@Tag(name = "project", description = "Endpoints use for projects")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Listing all projects", description = "Use to list all projects")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@RequestParam Long id) {
        Project Project = projectService.findById(id);
        return ResponseEntity.ok(ProjectDTO.fromProject(Project));
    }

    @Operation(summary = "Adding a project", description = "Use to add a project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PostMapping("/add")
    public ResponseEntity<ProjectDTO> save(@RequestBody ProjectCreationForm form) {
        Project savedProject = projectService.saveFromForm(form);
        return ResponseEntity.ok(ProjectDTO.fromProject(savedProject));
    }

    @Operation(summary = "Updating a project", description = "Use to update a project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(@RequestBody ProjectCreationForm form, @PathVariable Long id) {
        Project savedProject = projectService.updateFromForm(form, id);
        return ResponseEntity.ok(ProjectDTO.fromProject(savedProject));
    }

    @Operation(summary = "Deleting a project", description = "Use to delete a project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updating a project status", description = "Set status to closed")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PutMapping
    public ResponseEntity<Void> updateProjectStatus(@PathVariable Long id) {
        projectService.updateProjectStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/stages")
    public ResponseEntity<ProjectDTO> addStageToProject(
            @PathVariable Long projectId,
            @RequestBody StageCreationForm stageForm
    ) {
        Project updatedProject = projectService.addStageToProject(projectId, stageForm);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @DeleteMapping("/{projectId}/stages/{stageId}")
    public ResponseEntity<ProjectDTO> removeStageFromProject(
            @PathVariable Long projectId,
            @PathVariable Long stageId
    ) {
        Project updatedProject = projectService.removeStageFromProject(projectId, stageId);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @PostMapping("/{projectId}/employes/{email}")
    public ResponseEntity<ProjectDTO> addEmployeToProject(
            @PathVariable Long projectId,
            @PathVariable String email
    ) {
        Project updatedProject = projectService.addEmployeToProject(projectId, email);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

}
