package be.bdus.rush_api.api.models.user.dtos;

import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.UserRole;

public record UserDTO(
        Long id,
        String lastname,
        String firstname,
        String password,
        String email,
        String phoneNumber,
        String jobTitle,
        boolean isAvailable,
        EmployeStatus status,
        UserRole role
) {
    public static UserDTO toRecord(User user) {
        return new UserDTO(
                user.getId(),
                user.getLastname(),
                user.getFirstname(),
                user.getPassword(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getJobTitle(),
                user.isAvailable(),
                user.getStatus(),
                user.getRole()
        );
    }
}
