package be.bdus.rush_api.api.models.employee.dtos;

import be.bdus.rush_api.api.models.user.dtos.UserDTO;
import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.UserRole;

public record EmployeeDTO(
        Long id,
        String lastname,
        String firstname,
        String email,
        String phoneNumber,
        String jobTitle,
        boolean isAvailable
) {
public static EmployeeDTO fromEmployee(Employee employee) {
    return new EmployeeDTO(
            employee.getId(),
            employee.getLastname(),
            employee.getFirstname(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getJobTitle(),
            employee.isAvailable()
    );
}
}
