package be.bdus.rush_api.api.models.security.dtos;

import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.UserRole;

public record UserSessionDTO(
        Long id,
        String username,
        UserRole role,
        String firstname,
        String lastname,
        String email
) {

    public static UserSessionDTO fromUser(User user) {
        return new UserSessionDTO(user.getId(),user.getUsername(),user.getRole(),user.getFirstname(),user.getLastname(), user.getEmail());
    }
}
