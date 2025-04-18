package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.equipement.forms.EquipementForm;
import be.bdus.rush_api.bll.services.EquipementService;
import be.bdus.rush_api.dl.entities.Equipement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipements")

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "equipements", description = "Endpoints use for equipements")
public class EquipementController {

    private final EquipementService equipementService;

    @Operation(summary = "Listing all equipements", description = "Use to list all equipements")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping
    public ResponseEntity<CustomPage<EquipementDTO>> getAllEquipements(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Equipement> equipement = equipementService.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<EquipementDTO> dtos = equipement.getContent().stream()
                .map(EquipementDTO::fromEquipement)
                .toList();
        CustomPage<EquipementDTO> result = new CustomPage<>(dtos,equipement.getTotalPages(),equipement.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listing equipements by id", description = "Let the user search an equipement with its id")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<EquipementDTO> getById(@RequestParam Long id) {
        Equipement equipement = equipementService.findById(id);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipement));
    }

    @Operation(summary = "Listing equipements by serial number", description = "Let the user search an equipement with its serial number")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/serialNumber/{serialNumber}")
    public ResponseEntity<EquipementDTO> getBySerialNumber(@RequestParam String serialNumber) {
        Equipement equipement = equipementService.findBySerialNumber(serialNumber);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipement));
    }

    @Operation(summary = "Listing equipements by owner id", description = "Let the user search an equipement with the company that owns it")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/owner/{id}")
    public ResponseEntity<CustomPage<EquipementDTO>> getByOwnerId(@RequestParam Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Page<Equipement> equipement = equipementService.findByOwnerId(id, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<EquipementDTO> dtos = equipement.getContent().stream()
                .map(EquipementDTO::fromEquipement)
                .toList();
        CustomPage<EquipementDTO> result = new CustomPage<>(dtos,equipement.getTotalPages(),equipement.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "add equipment", description = "Let the user add an equipement to his project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PostMapping("/add")
    public ResponseEntity<EquipementDTO> save(@RequestBody EquipementForm equipementForm) {
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipementService.save(equipementForm.toEquipement())));
    }

    @Operation(summary = "update equipment", description = "Let the user update an equipement from his project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<EquipementDTO> update(@PathVariable Long id, @RequestBody EquipementForm equipementForm) {
        Equipement updatedEquipement = equipementService.update(equipementForm.toEquipement(), id);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(updatedEquipement));
    }

    @Operation(summary = "delete equipment", description = "Let the user delete an equipement from his project")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "plan next revisions", description = "Let the user plan next revisions")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PostMapping("/plan-revisions")
    public ResponseEntity<Void> planNextRevisions() {
        equipementService.planNextRevisionForEquipements();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "get all equipements with planned revisions", description = "Let the user get all equipements with planned revisions")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/planned")
    public ResponseEntity<List<Equipement>> getPlannedEquipements() {
        List<Equipement> list = equipementService.findAll(Pageable.unpaged()).stream()
                .filter(e -> e.getPlannedRevisionDate() != null)
                .toList();
        return ResponseEntity.ok(list);
    }
}
