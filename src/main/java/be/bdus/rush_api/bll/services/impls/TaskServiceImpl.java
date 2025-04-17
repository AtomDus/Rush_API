package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.task.forms.TaskForm;
import be.bdus.rush_api.bll.services.StageService;
import be.bdus.rush_api.bll.services.TaskService;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dal.repositories.TaskRepository;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.Task;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final StageRepository stageRepository;

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Stage saveFromForm(TaskForm form) {
        Stage stage = stageRepository.findByName(form.StageName())
                .orElseThrow(() -> new EntityNotFoundException("Stage not found"));

        Task task = new Task();
        task.setName(form.name());
        task.setDescription(form.description());
        task.setStatus(form.status());
        task.setCompletionDate(form.completionDate());
        task.setDueDate(form.completionDate());
        task.setStage(stage);

        stage.getTasks().add(task);

        return stageRepository.save(stage);
    }

    @Override
    public Stage updateFromForm(TaskForm form, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setName(form.name());
        task.setDescription(form.description());
        task.setStatus(form.status());
        task.setCompletionDate(form.completionDate());

        if (!task.getStage().getName().equals(form.StageName())) {
            Stage newStage = stageRepository.findByName(form.StageName())
                    .orElseThrow(() -> new EntityNotFoundException("Stage not found"));

            Stage oldStage = task.getStage();
            oldStage.getTasks().remove(task);

            task.setStage(newStage);
            newStage.getTasks().add(task);

            stageRepository.save(oldStage);
            stageRepository.save(newStage);
        } else {
            taskRepository.save(task);
        }

        return task.getStage();
    }


}
