package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.equipement.dtos.EquipementDTO;
import be.bdus.rush_api.api.models.CustomPage;
import be.bdus.rush_api.api.models.equipement.forms.EquipementForm;
import be.bdus.rush_api.bll.services.EquipementService;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.il.request.SearchParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipements")

@Tag(name = "equipements", description = "Endpoints use for equipements")
public class EquipementController {

    private final EquipementService equipementService;

    @Operation(summary = "Listing all equipements", description = "Use to list all equipements")
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
    @GetMapping("/{id}")
    public ResponseEntity<EquipementDTO> getById(@RequestParam Long id) {
        Equipement equipement = equipementService.findById(id);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipement));
    }

    @Operation(summary = "Listing equipements by serial number", description = "Let the user search an equipement with its serial number")
    @GetMapping("/serialNumber")
    public ResponseEntity<EquipementDTO> getBySerialNumber(@RequestParam String serialNumber) {
        Equipement equipement = equipementService.findBySerialNumber(serialNumber);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipement));
    }

    @Operation(summary = "Listing equipements by owner id", description = "Let the user search an equipement with the company that owns it")
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
    @PostMapping("/add")
    public ResponseEntity<EquipementDTO> save(@RequestBody EquipementForm equipementForm) {
        return ResponseEntity.ok(EquipementDTO.fromEquipement(equipementService.save(equipementForm.toEquipement())));
    }

    @Operation(summary = "update equipment", description = "Let the user update an equipement from his project")
    @PutMapping("/{id}")
    public ResponseEntity<EquipementDTO> update(@PathVariable Long id, @RequestBody EquipementForm equipementForm) {
        Equipement updatedEquipement = equipementService.update(equipementForm.toEquipement(), id);
        return ResponseEntity.ok(EquipementDTO.fromEquipement(updatedEquipement));
    }

    //ToDo
    @Operation(summary = "delete equipment", description = "Let the user delete an equipement from his project")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
