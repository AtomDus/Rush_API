package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.ProductionCompany;
import be.bdus.rush_api.il.request.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductionService {

    Page<ProductionCompany> findAll(Pageable pageable);

    ProductionCompany findById(Long id);

    Optional<ProductionCompany> findByName(String name);

    ProductionCompany save(ProductionCompany productionCompany);

    ProductionCompany update(ProductionCompany productionCompany, Long id);

    void delete(Long id);
}
