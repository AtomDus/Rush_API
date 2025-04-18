package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface EmployeeService {

    Optional<Employee> findById(Long id);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Page<Employee> findByJobTitle(Pageable pageable, String jobTitle);

    Page<Employee> findAvailableUsers(Pageable pageable);

    boolean setUserAvailable(Long id);
}
