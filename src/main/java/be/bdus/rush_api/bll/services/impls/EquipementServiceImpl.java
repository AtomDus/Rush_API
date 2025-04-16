package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.equipement.forms.EquipementForm;
import be.bdus.rush_api.bll.services.EquipementService;
import be.bdus.rush_api.dal.repositories.EquipementRepository;
import be.bdus.rush_api.dal.repositories.LCompanyRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.il.request.SearchParam;
import be.bdus.rush_api.il.specifications.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements EquipementService {

    private final EquipementRepository equipementRepository;
    private final UserRepository userRepository;
    private final LocationServiceImpl locationService;

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
        LocationCompany owner = equipement.getOwner();

        if (owner != null && owner.getName() != null) {
            Optional<LocationCompany> existingOwnerOpt = locationService.findByName(owner.getName());

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
        updatedEquipement.setStockagePlace(equipement.getStockagePlace());

        LocationCompany owner = equipement.getOwner();
        if (owner != null && owner.getName() != null) {
            Optional<LocationCompany> existingOwnerOpt = locationService.findByName(owner.getName());

            if (existingOwnerOpt.isPresent()) {
                updatedEquipement.setOwner(existingOwnerOpt.get());
            } else {
                updatedEquipement.setOwner(locationService.save(owner));
            }
        }

        return equipementRepository.save(updatedEquipement);
    }
}
