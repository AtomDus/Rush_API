package be.bdus.rush_api.api.models.project.forms;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.ProductionCompany;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ProjectCreationForm(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        LocalDate startingDate,

        LocalDate finishingDate,

        @NotNull
        StageStatus status,

        @NotNull
        String responsableEmail,

        @NotNull
        String productionCompanyName,

        int duration,

        @NotNull
        int budget
) {
}
