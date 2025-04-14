package be.bdus.rush_api.api.models.security.dtos;

public record UserTokenDTO(
        UserSessionDTO user,
        String token
) {
}
