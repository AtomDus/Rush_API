package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.RentingCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RentingService {
    Page<RentingCompany> findAll(Pageable pageable);

    RentingCompany findById(Long id);

    RentingCompany save(RentingCompany locationCompany);

    RentingCompany update(RentingCompany locationCompany, Long id);

    void delete(Long id);

    Optional<RentingCompany> findByName(String name);

    Page<RentingCompany> findByProjectsId(Pageable pageable, Long id);
}
