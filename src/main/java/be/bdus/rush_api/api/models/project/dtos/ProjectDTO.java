package be.bdus.rush_api.api.models.project.dtos;

import be.bdus.rush_api.api.models.employee.dtos.EmployeeDTO;
import be.bdus.rush_api.api.models.stage.dtos.StageCreationDTO;
import be.bdus.rush_api.api.models.stage.dtos.StageDTO;
import be.bdus.rush_api.api.models.user.dtos.UserDTO;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;
import java.util.List;

public record ProjectDTO (
        Long id,
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        UserDTO responsable,
        List<EmployeeDTO> employes,
        ProductionCompany productionCompany,
        List<Equipement> equipements,
        List<RentingCompany> locationCompanies,
        List<StageCreationDTO> stages,
        int nbOfStages,
        double pourcentageDone,
        int duration,
        int budget,
        String place
) {
    public static ProjectDTO fromProject(Project project) {
        List<StageCreationDTO> stageDTOs = project.getStages().stream()
                .map(StageCreationDTO::fromStage)
                .toList();

        List<EmployeeDTO> employeeDTOS = project.getEmployes().stream()
                .map(EmployeeDTO::fromEmployee)
                .toList();

        UserDTO responsableDTO = UserDTO.fromUser(project.getResponsable());

        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartingDate(),
                project.getFinishingDate(),
                project.getStatus(),
                responsableDTO,
                employeeDTOS,
                project.getProductionCompany(),
                project.getEquipements(),
                project.getLocationCompanies(),
                stageDTOs,
                project.getNbOfStages(),
                project.getPourcentageDone(),
                project.getDuration(),
                project.getBudget(),
                project.getPlace()
        );
    }
}
