package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.company.dtos.CompanyDTO;
import be.bdus.rush_api.api.models.company.forms.RCompanyForm;
import be.bdus.rush_api.bll.services.RentingService;
import be.bdus.rush_api.dl.entities.RentingCompany;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/renting-company")

@CrossOrigin("*")
@Tag(name = "Renting Company", description = "Endpoints use for renting company")
public class RentingCompanyController {

    private final RentingService locationService;

    @Operation(summary = "Listing all companies we have in our database", description = "Use to list all companies we have in our database")
    @GetMapping
    public ResponseEntity<CustomPage<CompanyDTO>> getAllLocationCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<RentingCompany> LocationCompany = locationService.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<CompanyDTO> dtos = LocationCompany.getContent().stream()
                .map(CompanyDTO::fromCompany)
                .toList();
        CustomPage<CompanyDTO> result = new CustomPage<>(dtos,LocationCompany.getTotalPages(),LocationCompany.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing companies by id", description = "Let the user search an company with its id")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getById(@RequestParam Long id) {
        RentingCompany LocationCompany = locationService.findById(id);
        return ResponseEntity.ok(CompanyDTO.fromCompany(LocationCompany));
    }

    @Operation(summary = "Listing companies by name", description = "Let the user search an company with its name")
    @GetMapping("/name/{name}")
    public ResponseEntity<CompanyDTO> getByName(@RequestParam String name) {
        return locationService.findByName(name)
                .map(company -> ResponseEntity.ok(CompanyDTO.fromCompany(company)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing companies by project id", description = "Let the user search an company with its project id")
    @GetMapping("/projects/{id}/locations")
    public ResponseEntity<CustomPage<CompanyDTO>> getLocationCompaniesByProjectId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Page<RentingCompany> locations = locationService.findByProjectsId(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)), id
        );

        List<CompanyDTO> dtos = locations.getContent().stream()
                .map(CompanyDTO::fromCompany)
                .toList();

        CustomPage<CompanyDTO> result = new CustomPage<>(dtos, locations.getTotalPages(), locations.getNumber() + 1);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Adding a new company", description = "Use to add a new company")
    @PostMapping("/add")
    public ResponseEntity<CompanyDTO> save(@RequestBody RCompanyForm locationCompany) {
        return ResponseEntity.ok(CompanyDTO.fromCompany(locationService.save(locationCompany.toCompany())));
    }

    @Operation(summary = "Deleting a company", description = "Use to delete a company")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updating a company", description = "Use to update a company")
    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyDTO> update(@PathVariable Long id, @RequestBody RCompanyForm locationCompany) {
        return ResponseEntity.ok(CompanyDTO.fromCompany(locationService.update(locationCompany.toCompany(), id)));
    }

}
