package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.ProductionCompany;
import be.bdus.rush_api.il.request.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductionService {

    Page<ProductionCompany> findAll(List<SearchParam<ProductionCompany>> searchParams, Pageable pageable);

    ProductionCompany findById(Long id);

    ProductionCompany save(ProductionCompany productionCompany);

    ProductionCompany update(ProductionCompany productionCompany, Long id);

    void delete(Long id);
}
