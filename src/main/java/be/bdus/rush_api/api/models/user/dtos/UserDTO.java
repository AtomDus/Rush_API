package be.bdus.rush_api.api.models.user.dtos;

import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.UserRole;

public record UserDTO(
        Long id,
        String username,
        String lastname,
        String firstname,
        String email,
        String phoneNumber,
        String jobTitle,
        boolean isAvailable,
        EmployeStatus status,
        UserRole role
) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getLastname(),
                user.getFirstname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getJobTitle(),
                user.isAvailable(),
                user.getStatus(),
                user.getRole()
        );
    }
}
