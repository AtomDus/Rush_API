package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.EquipementService;
import be.bdus.rush_api.dal.repositories.EquipementRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.RentingCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements EquipementService {

    private final EquipementRepository equipementRepository;
    private final UserRepository userRepository;
    private final RentingServiceImpl locationService;

    @Override
    public Page<Equipement> findAll(Pageable pageable) {
        return equipementRepository.findAll(pageable);
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipement not found"));
    }

    @Override
    public Equipement findBySerialNumber(String serialNumber) {
        return equipementRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipement not found"));
    }

    @Override
    public Page<Equipement> findByOwnerId(Long ownerId, Pageable pageable) {
        return equipementRepository.findByOwnerId(ownerId, pageable);
    }

    @Override
    public Equipement save(Equipement equipement) {
        RentingCompany owner = equipement.getOwner();

        if (owner != null && owner.getName() != null) {
            Optional<RentingCompany> existingOwnerOpt = locationService.findByName(owner.getName());

            if (existingOwnerOpt.isPresent()) {
                equipement.setOwner(existingOwnerOpt.get());
            } else {
                equipement.setOwner(locationService.save(owner));
            }
        }

        return equipementRepository.save(equipement);
    }

    @Override
    public void delete(Long id) {
        equipementRepository.deleteById(id);
    }

    @Override
    public Equipement update(Equipement equipement, Long id) {
        Equipement updatedEquipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipement not found"));

        updatedEquipement.setName(equipement.getName());
        updatedEquipement.setDescription(equipement.getDescription());
        updatedEquipement.setCondition(equipement.getCondition());
        updatedEquipement.setDateLastRevision(equipement.getDateLastRevision());
        updatedEquipement.setStock(equipement.getStock());
        updatedEquipement.setStockLocation(equipement.getStockLocation());

        RentingCompany owner = equipement.getOwner();
        if (owner != null && owner.getName() != null) {
            Optional<RentingCompany> existingOwnerOpt = locationService.findByName(owner.getName());

            if (existingOwnerOpt.isPresent()) {
                updatedEquipement.setOwner(existingOwnerOpt.get());
            } else {
                updatedEquipement.setOwner(locationService.save(owner));
            }
        }

        return equipementRepository.save(updatedEquipement);
    }

    public List<Equipement> findEquipementsToRevise() {
        LocalDate checkDate = LocalDate.now().minusMonths(6);
        return equipementRepository.findAll().stream()
                .filter(e -> e.getDateLastRevision() != null && e.getDateLastRevision().isBefore(checkDate))
                .collect(Collectors.toList());
    }

    public void planNextRevisionForEquipements() {
        List<Equipement> toRevise = findEquipementsToRevise();
        toRevise.forEach(eq -> {
            eq.setPlannedRevisionDate(LocalDate.now().plusWeeks(2));
            eq.setCondition(EquipementCondition.TO_BE_REVISED);
        });
        equipementRepository.saveAll(toRevise);
    }
}
