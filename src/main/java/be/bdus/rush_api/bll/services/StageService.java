package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.api.models.stage.dtos.StageCreationDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageForm;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StageService {

    Page<Stage> findAll(Pageable pageable);

    Stage findById(Long id);

    //Stage update(StageCreationDTO dto, Long id);

    void delete(Long id);

    Stage saveFromForm(StageCreationForm stageForm);

    Stage updateFromForm(StageCreationForm form, Long id);

    void validateStageDates(Stage stage, Project project);
}
