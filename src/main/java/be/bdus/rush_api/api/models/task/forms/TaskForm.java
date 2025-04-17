package be.bdus.rush_api.api.models.task.forms;

import be.bdus.rush_api.dl.entities.Task;
import be.bdus.rush_api.dl.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskForm(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        TaskStatus status,

        @NotNull
        LocalDate completionDate,

        @NotBlank
        String StageName
) {
}
