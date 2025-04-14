package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.Equipement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EquipementRepository extends JpaRepository<Equipement, Long>, JpaSpecificationExecutor<Equipement> {

    Optional<Equipement> findBySerialNumber(String serialNumber);

    Page<Equipement> findByOwnerId(Long ownerId, Pageable pageable);
}
