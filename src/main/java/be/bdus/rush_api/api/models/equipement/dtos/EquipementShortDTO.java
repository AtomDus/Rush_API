package be.bdus.rush_api.api.models.equipement.dtos;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;

public record EquipementShortDTO (
        Long id,
        String name,
        LocationCompany owner,
        String description
) {
    public static EquipementShortDTO fromEquipement(Equipement equipement) {
        return new EquipementShortDTO(
                equipement.getId(),
                equipement.getName(),
                equipement.getOwner(),
                equipement.getDescription()
        );
    }
}
