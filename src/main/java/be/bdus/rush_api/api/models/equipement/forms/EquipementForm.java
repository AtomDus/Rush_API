package be.bdus.rush_api.api.models.equipement.forms;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record EquipementForm (

        @NotBlank
        String name,

        @NotBlank
        LocationCompany owner,

        @NotBlank
        String description,

        String model,

        String serialNumber,

        @NotBlank
        String type,

        @NotBlank
        EquipementCondition condition,

        @NotBlank
        int stock,

        @NotBlank
        String stockLocation,

        Date acquisitionDate,

        Date lastRevision
){
    public static EquipementForm toEquipementForm(Equipement equipement) {
        return new EquipementForm(
                equipement.getName(),
                equipement.getOwner(),
                equipement.getDescription(),
                equipement.getModel(),
                equipement.getSerialNumber(),
                equipement.getType(),
                equipement.getCondition(),
                equipement.getStock(),
                equipement.getStockagePlace(),
                equipement.getDateAcquisition(),
                equipement.getDateLastRevision());
    }
}
