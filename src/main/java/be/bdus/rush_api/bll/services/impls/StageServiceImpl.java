package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.stage.dtos.StageCreationDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageForm;
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
    public Stage createStage(StageCreationDTO dto) {
        Stage stage = new Stage();
        stage.setName(dto.name());
        stage.setDescription(dto.description());
        stage.setStartingDate(dto.startingDate());
        stage.setFinishingDate(dto.finishingDate());
        stage.setStatus(dto.status());

        User responsable = userRepository.findByEmail(dto.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));
        stage.setResponsable(responsable);

        Project project = projectRepository.findByName(dto.projectName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        stage.setProject(project);

        return stageRepository.save(stage);
    }

    @Override
    public Stage update(Stage stage, Long id) {
        Optional<Stage> stageOptional = stageRepository.findById(id);
        if (stageOptional.isPresent()) {
            Stage existingStage = stageOptional.get();
            existingStage.setName(stage.getName());
            existingStage.setDescription(stage.getDescription());
            existingStage.setStartingDate(stage.getStartingDate());
            existingStage.setFinishingDate(stage.getFinishingDate());
            existingStage.setStatus(stage.getStatus());
            existingStage.setResponsable(stage.getResponsable());
            return stageRepository.save(existingStage);
        } else {
            throw new RuntimeException("Stage not found");
        }
    }

    @Override
    public void delete(Long id) {
        stageRepository.deleteById(id);
    }

    @Override
    public Stage saveFromForm(StageCreationForm form) {
        User responsable = userRepository.findByEmail(form.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));

        Project project = projectRepository.findByName(form.projectName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Stage stage = new Stage();
        stage.setName(form.name());
        stage.setDescription(form.description());
        stage.setStartingDate(form.startingDate());
        stage.setFinishingDate(form.finishingDate());
        stage.setStatus(form.status());
        stage.setResponsable(responsable);
        stage.setProject(project);

        return stageRepository.save(stage);
    }
}
