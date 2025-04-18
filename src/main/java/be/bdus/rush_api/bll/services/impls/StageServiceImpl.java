package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.stage.dtos.StageCreationDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.bll.services.StageService;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Page<Stage> findAll(Pageable pageable) {
        return stageRepository.findAll(pageable);
    }

    @Override
    public Stage findById(Long id) {
        return stageRepository.findById(id).orElseThrow(() -> new RuntimeException("Stage not found"));
    }

    @Override
    public void delete(Long id) {
        stageRepository.deleteById(id);
    }

    @Override
    public Stage saveFromForm(StageCreationForm form) {
        User responsable = userRepository.findByEmail(form.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));

        Stage stage = new Stage();
        stage.setName(form.name());
        stage.setDescription(form.description());
        stage.setStartingDate(form.startingDate());
        stage.setFinishingDate(form.finishingDate());
        stage.setStatus(form.status());
        stage.setResponsable(responsable);


        return stageRepository.save(stage);
    }

    @Override
    public Stage updateFromForm(StageCreationForm form, Long id) {
        Stage existingStage = stageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stage not found"));

        User responsable = userRepository.findByEmail(form.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));


        existingStage.setName(form.name());
        existingStage.setDescription(form.description());
        existingStage.setStartingDate(form.startingDate());
        existingStage.setFinishingDate(form.finishingDate());
        existingStage.setStatus(form.status());
        existingStage.setResponsable(responsable);

        return stageRepository.save(existingStage);
    }

    @Override
    public void validateStageDates(Stage stage, Project project) {
        if (stage.getStartingDate().isBefore(project.getStartingDate()) ||
                stage.getFinishingDate().isAfter(project.getFinishingDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stage dates must be within project range");
        }
        if (stage.getStartingDate().isAfter(stage.getFinishingDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stage starting date cannot be after finishing date");
        }
    }
}
