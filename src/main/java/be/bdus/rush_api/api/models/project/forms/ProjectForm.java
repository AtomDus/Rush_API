package be.bdus.rush_api.api.models.project.forms;

import be.bdus.rush_api.api.models.project.dtos.ProjectDTO;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record ProjectForm(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        LocalDate startingDate,

        @NotNull
        LocalDate finishingDate,

        @NotNull
        StageStatus status,

        @NotNull
        User responsable,

        @NotNull
        List<User> employes,

        @NotNull
        ProductionCompany productionCompany,

        @NotNull
        List<Equipement> equipements,

        @NotNull
        List<Stage> stages,

        @NotNull
        int nbOfStages,

        @NotNull
        double pourcentageDone,

        @NotNull
        int duration,

        @NotNull
        int budget,

        @NotBlank
        String place
) {
    public Project toProject() {
        Project project = new Project(
                name,
                description,
                startingDate,
                finishingDate,
                status,
                responsable,
                employes,
                productionCompany,
                equipements,
                stages,
                nbOfStages,
                pourcentageDone,
                duration,
                budget,
                place);
        return project;
    }
}
