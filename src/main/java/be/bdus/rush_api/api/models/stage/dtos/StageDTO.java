package be.bdus.rush_api.api.models.stage.dtos;

import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;
import java.util.Date;

public record StageDTO(
        Long id,
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        User responsable
) {
    public static StageDTO fromStage(Stage stage) {
        return new StageDTO(
                stage.getId(),
                stage.getName(),
                stage.getDescription(),
                stage.getStartingDate(),
                stage.getFinishingDate(),
                stage.getStatus(),
                stage.getResponsable()
        );
    }
}
