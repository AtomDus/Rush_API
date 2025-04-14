package be.bdus.rush_api.api.models.company.forms;

import be.bdus.rush_api.dl.entities.LocationCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LCompanyForm(
        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotBlank
        String zipCode,

        @NotBlank
        String city,

        @NotBlank
        String country,

        @NotNull
        String phoneNumber,

        @NotBlank
        String email
) {
    public LocationCompany toCompany() {
        LocationCompany company = new LocationCompany(name, address, zipCode, city, country, phoneNumber, email);
        return company;
    }
}
