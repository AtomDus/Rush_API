package be.bdus.rush_api.api.models.company.forms;

import be.bdus.rush_api.dl.entities.RentingCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RCompanyForm(
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
    public RentingCompany toCompany() {
        RentingCompany company = new RentingCompany(name, address, zipCode, city, country, phoneNumber, email);
        return company;
    }
}
