package be.bdus.rush_api.api.models.equipement.forms;

import be.bdus.rush_api.api.models.company.forms.LCompanyForm;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record EquipementForm (

        @NotBlank
        String name,

        @NotBlank
        LCompanyForm owner,

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
    public Equipement toEquipement() {
        LocationCompany company = owner.toCompany();
        Equipement equipement = new Equipement(name, company, description, model, serialNumber, type, condition, stock, stockLocation, acquisitionDate, lastRevision);
        return equipement;
    }

}
