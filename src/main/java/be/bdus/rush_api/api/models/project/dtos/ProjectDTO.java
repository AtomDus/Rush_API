package be.bdus.rush_api.api.models.project.dtos;

import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.StageStatus;

import java.util.Date;
import java.util.List;

public record ProjectDTO (
        Long id,
        String name,
        String description,
        Date startingDate,
        Date finishingDate,
        StageStatus status,
        User responsable,
        List<User> employes,
        ProductionCompany productionCompany,
        List<Equipement> equipements,
        List<LocationCompany> locationCompanies,
        List<Stage> stages,
        int nbOfStages,
        double pourcentageDone,
        int duration,
        int budget,
        String place
) {
    public static ProjectDTO fromProject(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartingDate(),
                project.getFinishingDate(),
                project.getStatus(),
                project.getResponsable(),
                project.getEmployes(),
                project.getProductionCompany(),
                project.getEquipements(),
                project.getLocationCompanies(),
                project.getStages(),
                project.getNbOfStages(),
                project.getPourcentageDone(),
                project.getDuration(),
                project.getBudget(),
                project.getPlace()
        );
    }
}
