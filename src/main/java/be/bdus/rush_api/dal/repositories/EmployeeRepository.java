package be.bdus.rush_api.dal.repositories;


import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Page<Employee> findByJobTitle(Pageable pageable, String jobTitle);

    Page<Employee> findByAvailableTrue(Pageable pageable);
}
