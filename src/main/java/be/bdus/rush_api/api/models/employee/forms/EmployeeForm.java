package be.bdus.rush_api.api.models.employee.forms;

import be.bdus.rush_api.dl.entities.Employee;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeForm(

        @NotBlank @Size(max = 50)
        String firstname,
        @NotBlank @Size(max = 50)
        String lastname,
        @NotBlank @Size(max = 150)
        String email,
        @NotBlank
        String phoneNumber,
        @NotBlank @Size(max = 50)
        String jobTitle,
        @NotNull
        boolean isAvailable

) {

    public Employee toEmployee() {
        return new Employee(
                firstname,
                lastname,
                email,
                phoneNumber,
                jobTitle,
                isAvailable
        );
    }
}
