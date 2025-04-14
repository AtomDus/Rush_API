package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.il.request.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipementService {

    Page<Equipement> findAll(List<SearchParam<Equipement>> searchParams, Pageable pageable);

    Equipement findById(Long id);

    Equipement findBySerialNumber(String serialNumber);

    Page<Equipement> findByOwnerId(Long ownerId, Pageable pageable);

    Equipement save(Equipement equipement);

    void delete(Long id);

    Equipement update(Equipement equipement, Long id);

}
