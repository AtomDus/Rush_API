package be.bdus.rush_api.api.models.security.forms;

import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterForm(
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
        boolean isAvailable,
        @NotNull
        EmployeStatus status,
        @NotBlank
        String password

) {

    public User toUser() {
        return new User(
                firstname,
                lastname,
                email,
                phoneNumber,
                jobTitle,
                isAvailable,
                status,
                password
        );
    }
}
