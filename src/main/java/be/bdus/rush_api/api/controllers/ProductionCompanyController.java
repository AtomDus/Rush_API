package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.company.dtos.CompanyDTO;
import be.bdus.rush_api.api.models.company.forms.PCompanyForm;
import be.bdus.rush_api.bll.services.ProductionService;
import be.bdus.rush_api.dl.entities.ProductionCompany;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/production-companies")

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "production", description = "Endpoints use for production company")
public class ProductionCompanyController {

    private final ProductionService productionService;

    @Operation(summary = "listing all production companies", description = "Use to list all production companies")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<CustomPage<CompanyDTO>> getAllProductionCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<ProductionCompany> productionCompany = productionService.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<CompanyDTO> dtos = productionCompany.getContent().stream()
                .map(CompanyDTO::fromProductionCompany)
                .toList();
        CustomPage<CompanyDTO> result = new CustomPage<>(dtos,productionCompany.getTotalPages(),productionCompany.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "listing a production company by id", description = "Use to list a production company by id")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getById(@RequestParam Long id) {
        ProductionCompany productionCompany = productionService.findById(id);
        return ResponseEntity.ok(CompanyDTO.fromProductionCompany(productionCompany));
    }

    @Operation(summary = "listing a production company by name", description = "Use to list a production company by name")
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/name/{name}")
    public ResponseEntity<CompanyDTO> getByName(@RequestParam String name) {
        return productionService.findByName(name)
                .map(company -> ResponseEntity.ok(CompanyDTO.fromProductionCompany(company)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "adding a production company", description = "Use to add a production company")
    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<CompanyDTO> save(@RequestBody PCompanyForm productionCompany) {
        return ResponseEntity.ok(CompanyDTO.fromProductionCompany(productionService.save(productionCompany.toCompany())));
    }

    @Operation(summary = "deleting a production company", description = "Use to delete a production company")
    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "updating a production company", description = "Use to update a production company")
    //@PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyDTO> update(@PathVariable Long id, @RequestBody PCompanyForm productionCompany) {
        return ResponseEntity.ok(CompanyDTO.fromProductionCompany(productionService.update(productionCompany.toCompany(), id)));
    }
}
