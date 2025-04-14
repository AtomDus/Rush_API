package be.bdus.rush_api.api.controllers.auth;

import be.bdus.rush_api.api.models.security.dtos.UserSessionDTO;
import be.bdus.rush_api.api.models.security.dtos.UserTokenDTO;
import be.bdus.rush_api.api.models.security.forms.LoginForm;
import be.bdus.rush_api.api.models.security.forms.RegisterForm;
import be.bdus.rush_api.bll.services.security.AuthService;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.il.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints use for authentification")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtil;

    @Operation(summary = "Inscription d'un utilisateur", description = "Permet à un utilisateur de s'inscrire.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur enregistré avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterForm form) {
        authService.register(form.toUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Connexion d'un utilisateur", description = "Authentifie un utilisateur et retourne un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
    })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(@Valid @RequestBody LoginForm form) {
        User user = authService.login(form.email(), form.password());
        UserSessionDTO userDTO = UserSessionDTO.fromUser(user);
        String token = jwtUtil.generateToken(user);
        UserTokenDTO userTokenDTO;
        userTokenDTO = new UserTokenDTO(userDTO, token);
        return ResponseEntity.ok(userTokenDTO);
    }

}
