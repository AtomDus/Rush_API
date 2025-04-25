package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.api.models.employee.forms.EmployeeForm;
import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.equipement.forms.EquipementForm;
import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.il.request.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Page<Project> findAll(Pageable pageable);

    Project findById(Long id);

    Project save(Project project);

    Project update(Project project, Long id);

    void delete(Long id);

    Project updateProjectStatus(Long id);

    Project saveFromForm(ProjectCreationForm projectForm);

    Project updateFromForm(ProjectCreationForm form, Long id);

    Project addStageToProject(Long projectId, StageCreationForm stageForm);

    Project removeStageFromProject(Long projectId, Long stageId);

    Project addEmployeToProject(Long projectId, EmployeeForm form);

    Page<Project> getPendingProjects(Pageable pageable);

    Page<Project> getOpenProjects(Pageable pageable);

    Page<Project> getClosedProjects(Pageable pageable);

    Page<Project> getProjectsByResponsableId(Pageable pageable, Long id);

    Page<Project> getPendingProjectsByResponsableId(Pageable pageable, Long id);

    Page<Project> getOpenProjectsByResponsableId(Pageable pageable, Long id);

    Page<Project> getClosedProjectsByResponsableId(Pageable pageable, Long id);

    Project addEquipmentToProject(Long projectId, EquipementForm equipementForm);

}
