package be.bdus.rush_api.api.models.project.forms;

import be.bdus.rush_api.api.models.project.dtos.ProjectDTO;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record ProjectForm(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        Date startingDate,

        @NotNull
        Date finishingDate,

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
    public static ProjectForm toProject(Project project) {
        return new ProjectForm(
                project.getName(),
                project.getDescription(),
                project.getStartingDate(),
                project.getFinishingDate(),
                project.getStatus(),
                project.getResponsable(),
                project.getEmployes(),
                project.getProductionCompany(),
                project.getEquipements(),
                project.getStages(),
                project.getNbOfStages(),
                project.getPourcentageDone(),
                project.getDuration(),
                project.getBudget(),
                project.getPlace()
        );

    }
}
