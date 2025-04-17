package be.bdus.rush_api.api.models.project.dtos;

import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;

public record ProjectCreationDTO(
        Long id,
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        String responsableEmail,
        String ProductionCompanyName,
        int duration,
        int budget
) {
}
