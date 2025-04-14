package be.bdus.rush_api.api.models.company.dtos;


import be.bdus.rush_api.dl.entities.LocationCompany;

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
}
