package be.bdus.rush_api.api.models.task.dtos;

import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.Task;

import java.time.LocalDate;

public record TaskDTO(
        Long id,
        String name,
        String description,
        LocalDate completionDate,
        String StageName
) {
    public static TaskDTO fromTask(Task task) {
        return new TaskDTO(task.getId(), task.getName(), task.getDescription(), task.getCompletionDate(), task.getStage().getName());
    }
}
