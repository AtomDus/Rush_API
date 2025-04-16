package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.LocationCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LCompanyRepository extends JpaRepository<LocationCompany, Long>, JpaSpecificationExecutor<LocationCompany> {

    Optional<LocationCompany> findByName(String name);

    Page<LocationCompany> findByProjectsId(Long projectId, Pageable pageable);
}
