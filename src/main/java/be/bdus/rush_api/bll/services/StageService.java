package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StageService {

    Page<Stage> findAll(Pageable pageable);

    Stage findById(Long id);

    Stage save(Stage stage);

    Stage update(Stage stage, Long id);

    void delete(Long id);
}
