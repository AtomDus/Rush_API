package be.bdus.rush_api.api.models.stage.dtos;

import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;

public record StageCreationDTO(
        Long id,
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        String responsableEmail
) {
    public static StageCreationDTO fromStage(Stage stage) {
        return new StageCreationDTO(
                stage.getId(),
                stage.getName(),
                stage.getDescription(),
                stage.getStartingDate(),
                stage.getFinishingDate(),
                stage.getStatus(),
                stage.getResponsable().getEmail()
        );
    }
}
