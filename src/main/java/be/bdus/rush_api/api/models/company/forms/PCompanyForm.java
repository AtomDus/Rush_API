package be.bdus.rush_api.api.models.company.forms;

import be.bdus.rush_api.dl.entities.ProductionCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PCompanyForm(@NotBlank
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
    public ProductionCompany toCompany() {
        ProductionCompany company = new ProductionCompany(name, address, zipCode, city, country, phoneNumber, email);
        return company;
    }
}
