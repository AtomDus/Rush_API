package be.bdus.rush_api.api.models.equipement.dtos;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.RentingCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;

import java.time.LocalDate;

public record EquipementDTO(
    Long id,
    String name,
    RentingCompany owner,
    String description,
    String model,
    String serialNumber,
    String type,
    EquipementCondition condition,
    int stock,
    String stockLocation,
    LocalDate acquisitionDate,
    LocalDate lastRevision
) {

    public static EquipementDTO fromEquipement(Equipement equipement) {
        return new EquipementDTO(
            equipement.getId(),
            equipement.getName(),
            equipement.getOwner(),
            equipement.getDescription(),
            equipement.getModel(),
            equipement.getSerialNumber(),
            equipement.getType(),
            equipement.getCondition(),
            equipement.getStock(),
            equipement.getStockLocation(),
            equipement.getDateAcquisition(),
            equipement.getDateLastRevision()
        );
    }
}
