package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.il.request.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Page<LocationCompany> findAll(Pageable pageable);

    LocationCompany findById(Long id);

    LocationCompany save(LocationCompany locationCompany);

    LocationCompany update(LocationCompany locationCompany, Long id);

    void delete(Long id);

    Optional<LocationCompany> findByName(String name);

    Page<LocationCompany> findByProjectsId(Pageable pageable, Long id);
}
