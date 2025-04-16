package be.bdus.rush_api.api.models.stage.dtos;

import be.bdus.rush_api.dl.enums.StageStatus;

import java.time.LocalDate;

public record StageCreationDTO(
        String name,
        String description,
        LocalDate startingDate,
        LocalDate finishingDate,
        StageStatus status,
        String responsableEmail,
        String projectName
) {}
