package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
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
}
