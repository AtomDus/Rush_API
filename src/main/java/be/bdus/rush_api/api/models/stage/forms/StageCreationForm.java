package be.bdus.rush_api.api.models.stage.forms;

import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;

public record StageCreationForm (
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        String responsableEmail,
        String projectName
){
}
