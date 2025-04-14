package be.bdus.rush_api.api.models.security.dtos;

import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.UserRole;

public record UserSessionDTO(
        Long id,
        UserRole role,
        String firstname,
        String lastname
) {

    public static UserSessionDTO fromUser(User user) {
        return new UserSessionDTO(user.getId(),user.getRole(),user.getFirstname(),user.getLastname());
    }
}
