package be.bdus.rush_api.api.models.company.forms;

import be.bdus.rush_api.dl.entities.LocationCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyForm (
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
        int phoneNumber,

        @NotBlank
        String email
) {
    public static CompanyForm toCompany(LocationCompany company) {
        return new CompanyForm(
                company.getName(),
                company.getAddress(),
                company.getZipCode(),
                company.getCity(),
                company.getCountry(),
                company.getPhoneNumber(),
                company.getEmail());
    }
}
