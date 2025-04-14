package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.EquipementService;
import be.bdus.rush_api.dal.repositories.EquipementRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.il.request.SearchParam;
import be.bdus.rush_api.il.specifications.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements EquipementService {

    private final EquipementRepository equipementRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Equipement> findAll(List<SearchParam<Equipement>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return equipementRepository.findAll(pageable);
        }
        return equipementRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Override
    public Equipement findBySerialNumber(String serialNumber) {
        return equipementRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Override
    public Page<Equipement> findByOwnerId(Long ownerId, Pageable pageable) {
        return equipementRepository.findByOwnerId(ownerId, pageable);
    }

    @Override
    public Equipement save(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public void delete(Long id) {
        equipementRepository.deleteById(id);
    }

    @Override
    public Equipement update(Equipement equipement, Long id) {
        Optional<Equipement> selectedEquipement = equipementRepository.findById(id);
        if (selectedEquipement.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Equipement updatedEquipement = selectedEquipement.get();
        updatedEquipement.setName(equipement.getName());
        updatedEquipement.setDescription(equipement.getDescription());
        updatedEquipement.setCondition(equipement.getCondition());
        updatedEquipement.setDateLastRevision(equipement.getDateLastRevision());
        updatedEquipement.setStock(equipement.getStock());
        updatedEquipement.setStockagePlace(equipement.getStockagePlace());
        return equipementRepository.save(equipement);
    }
}
