package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.employee.forms.EmployeeForm;
import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.equipement.forms.EquipementForm;
import be.bdus.rush_api.api.models.project.dtos.ProjectDTO;
import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.project.forms.ProjectForm;
import be.bdus.rush_api.api.models.stage.dtos.StageDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.bll.services.EmployeeService;
import be.bdus.rush_api.bll.services.ProjectService;
import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "project", description = "Endpoints use for projects")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    @Operation(summary = "Listing all projects", description = "Use to list all projects")
    //@PreAuthorize("isAuthenticated()")
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
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable  Long id) {
        Project Project = projectService.findById(id);
        return ResponseEntity.ok(ProjectDTO.fromProject(Project));
    }

    @Operation(summary = "Adding a project", description = "Use to add a project")
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<ProjectDTO> save(@RequestBody ProjectCreationForm form) {
        Project savedProject = projectService.saveFromForm(form);
        return ResponseEntity.ok(ProjectDTO.fromProject(savedProject));
    }

    @Operation(summary = "Updating a project", description = "Use to update a project")
    //@PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(@RequestBody ProjectCreationForm form, @PathVariable Long id) {
        Project savedProject = projectService.updateFromForm(form, id);
        return ResponseEntity.ok(ProjectDTO.fromProject(savedProject));
    }

    @Operation(summary = "Deleting a project", description = "Use to delete a project")
    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updating a project status", description = "Set status to closed")
    //@PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<Void> updateProjectStatus(@PathVariable Long id) {
        projectService.updateProjectStatus(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adding a stage to a project", description = "Use to add a stage to a project")
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/stages")
    public ResponseEntity<ProjectDTO> addStageToProject(
            @PathVariable Long id,
            @RequestBody StageCreationForm stageForm
    ) {
        Project updatedProject = projectService.addStageToProject(id, stageForm);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(summary = "Removing a stage from a project", description = "Use to remove a stage from a project")
    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{projectId}/stages/{stageId}")
    public ResponseEntity<ProjectDTO> removeStageFromProject(
            @PathVariable Long projectId,
            @PathVariable Long stageId
    ) {
        Project updatedProject = projectService.removeStageFromProject(projectId, stageId);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(summary = "Add an employee to a project", description = "Provide employee details")
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/employes")
    public ResponseEntity<ProjectDTO> addEmployeToProject(
            @PathVariable Long id,
            @RequestBody EmployeeForm form
    ) {
        Project updatedProject = projectService.addEmployeToProject(id, form);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(summary = "Add an equipment to a project", description = "Provide equipment details")
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/equipements")
    public ResponseEntity<ProjectDTO> addEquipmentToProject(
            @PathVariable Long id,
            @RequestBody EquipementForm form
    ) {
        Project updatedProject = projectService.addEquipmentToProject(id, form);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(summary = "Listing all pending projects", description = "Use to list all pending projects")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/pending")
    public ResponseEntity<CustomPage<ProjectDTO>> getPendingProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Project> projects = projectService.getPendingProjects(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );

        List<ProjectDTO> dtos = projects.getContent()
                .stream()
                .map(ProjectDTO::fromProject)
                .toList();

        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing all open projects", description = "Use to list all open projects")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/open")
    public ResponseEntity<CustomPage<ProjectDTO>> getOpenProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Project> projects = projectService.getOpenProjects(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );

        List<ProjectDTO> dtos = projects.getContent()
                .stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing all closed projects", description = "Use to list all closed projects")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/closed")
    public ResponseEntity<CustomPage<ProjectDTO>> getClosedProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Project> projects = projectService.getClosedProjects(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );

        List<ProjectDTO> dtos = projects.getContent()
                .stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing all projects by responsable", description = "Use to list all projects by responsable")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/by-responsable/{id}")
    public ResponseEntity<Page<ProjectDTO>> getByResponsable(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).ascending());

        Page<Project> projectPage = projectService.getProjectsByResponsableId(pageable, id);

        Page<ProjectDTO> projectDTOPage = projectPage.map(ProjectDTO::fromProject);

        return ResponseEntity.ok(projectDTOPage);
    }

    @Operation(summary = "Listing all pending projects by responsable", description = "Use to list all pending projects by responsable")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/by-responsable/{id}/pending")
    public ResponseEntity<CustomPage<ProjectDTO>> getPendingByResponsable(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Page<Project> projects = projectService.getPendingProjectsByResponsableId(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)), id
        );
        List<ProjectDTO> dtos = projects.getContent().stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing all open projects by responsable", description = "Use to list all open projects by responsable")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/by-responsable/{id}/open")
    public ResponseEntity<CustomPage<ProjectDTO>> getOpenByResponsable(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Page<Project> projects = projectService.getOpenProjectsByResponsableId(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)), id
        );
        List<ProjectDTO> dtos = projects.getContent().stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing all closed projects by responsable", description = "Use to list all closed projects by responsable")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/by-responsable/{id}/closed")
    public ResponseEntity<CustomPage<ProjectDTO>> getClosedByResponsable(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Page<Project> projects = projectService.getClosedProjectsByResponsableId(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)), id
        );
        List<ProjectDTO> dtos = projects.getContent().stream()
                .map(ProjectDTO::fromProject)
                .toList();
        CustomPage<ProjectDTO> result = new CustomPage<>(dtos, projects.getTotalPages(), projects.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

}
