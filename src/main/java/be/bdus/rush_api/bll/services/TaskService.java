package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.api.models.task.forms.TaskForm;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Long id);

    void delete(Long id);

    Stage saveFromForm(TaskForm form);

    Stage updateFromForm(TaskForm form, Long id);
}
