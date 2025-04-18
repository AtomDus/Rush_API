package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.EmployeeService;
import be.bdus.rush_api.dal.repositories.EmployeeRepository;
import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public Optional<Employee> findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<Employee> findByJobTitle(Pageable pageable, String jobTitle) {
        return employeeRepository.findByJobTitle(pageable, jobTitle);
    }

    public Page<Employee> findAvailableUsers(Pageable pageable) {
        return employeeRepository.findByAvailableTrue(pageable);
    }

    public boolean setUserAvailable(Long id) {
        Optional<Employee> user = employeeRepository.findById(id);
        if (user.isPresent()) {
            user.get().setAvailable(true);
            employeeRepository.save(user.get());
            return true;
        }
        return false;
    }
}
