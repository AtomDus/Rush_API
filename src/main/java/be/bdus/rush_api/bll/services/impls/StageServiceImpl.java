package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.StageService;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dl.entities.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;

    @Override
    public Page<Stage> findAll(Pageable pageable) {
        return stageRepository.findAll(pageable);
    }

    @Override
    public Stage findById(Long id) {
        return stageRepository.findById(id).orElseThrow(() -> new RuntimeException("Stage not found"));
    }

    @Override
    public Stage save(Stage stage) {
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
}
