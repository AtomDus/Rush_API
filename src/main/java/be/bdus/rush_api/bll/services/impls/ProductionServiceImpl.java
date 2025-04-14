package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.ProductionService;
import be.bdus.rush_api.dal.repositories.PCompanyRepository;
import be.bdus.rush_api.dl.entities.ProductionCompany;
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
public class ProductionServiceImpl implements ProductionService {

    private final PCompanyRepository productionRepository;

    @Override
    public Page<ProductionCompany> findAll(List<SearchParam<ProductionCompany>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return productionRepository.findAll(pageable);
        }
        return productionRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public ProductionCompany findById(Long id) {
        return productionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @Override
    public ProductionCompany save(ProductionCompany productionCompany) {
        return productionRepository.save(productionCompany);
    }

    @Override
    public ProductionCompany update(ProductionCompany productionCompany, Long id) {
        Optional<ProductionCompany> selectedProductionCompany = productionRepository.findById(id);
        if (selectedProductionCompany.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        ProductionCompany updatedProductionCompany = selectedProductionCompany.get();
        updatedProductionCompany.setName(productionCompany.getName());
        updatedProductionCompany.setAddress(productionCompany.getAddress());
        updatedProductionCompany.setZipCode(productionCompany.getZipCode());
        updatedProductionCompany.setCity(productionCompany.getCity());
        updatedProductionCompany.setCountry(productionCompany.getCountry());
        updatedProductionCompany.setPhoneNumber(productionCompany.getPhoneNumber());
        updatedProductionCompany.setEmail(productionCompany.getEmail());
        return productionRepository.save(productionCompany);
    }

    @Override
    public void delete(Long id) {
        productionRepository.deleteById(id);
    }
}
