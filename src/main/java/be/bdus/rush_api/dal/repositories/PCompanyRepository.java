package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PCompanyRepository extends JpaRepository<ProductionCompany, Long>, JpaSpecificationExecutor<ProductionCompany> {
}
