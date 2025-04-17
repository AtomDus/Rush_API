package be.bdus.rush_api.api.models.equipement.forms;

import be.bdus.rush_api.api.models.company.forms.RCompanyForm;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.RentingCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record EquipementForm (

        @NotBlank
        String name,

        @NotBlank
        RCompanyForm owner,

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

        LocalDate acquisitionDate,

        LocalDate lastRevision
){
    public Equipement toEquipement() {
        RentingCompany company = owner.toCompany();
        Equipement equipement = new Equipement(name, company, description, model, serialNumber, type, condition, stock, stockLocation, acquisitionDate, lastRevision);
        return equipement;
    }

}
