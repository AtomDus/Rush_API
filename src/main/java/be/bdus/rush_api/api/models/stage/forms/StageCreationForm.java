package be.bdus.rush_api.api.models.stage.forms;

import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StageCreationForm (

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        LocalDate startingDate,

        LocalDate finishingDate,

        @NotNull
        StageStatus status,

        @NotBlank
        String responsableEmail
){
}
