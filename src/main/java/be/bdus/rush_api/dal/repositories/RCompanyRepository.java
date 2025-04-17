package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.RentingCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RCompanyRepository extends JpaRepository<RentingCompany, Long>, JpaSpecificationExecutor<RentingCompany> {

    Optional<RentingCompany> findByName(String name);

    Page<RentingCompany> findByProjectsId(Long projectId, Pageable pageable);
}
