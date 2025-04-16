package be.bdus.rush_api.api.models.company.dtos;


import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.dl.entities.ProductionCompany;

public record CompanyDTO (
        Long id,
        String name,
        String address,
        String zipCode,
        String city,
        String country,
        String phoneNumber,
        String email
) {
    public static CompanyDTO fromCompany(LocationCompany company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getZipCode(),
                company.getCity(),
                company.getCountry(),
                company.getPhoneNumber(),
                company.getEmail()
        );
    }

    public static CompanyDTO fromProductionCompany(ProductionCompany company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getZipCode(),
                company.getCity(),
                company.getCountry(),
                company.getPhoneNumber(),
                company.getEmail()
        );
    }
}
