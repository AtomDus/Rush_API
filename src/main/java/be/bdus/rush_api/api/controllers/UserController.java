package be.bdus.rush_api.api.controllers;

import be.bdus.rush_api.api.models.user.dtos.UserDTO;
import be.bdus.rush_api.bll.services.UserService;
import be.bdus.rush_api.dl.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "users", description = "Endpoints use for users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Listing users by id", description = "Let the user search an user with its id")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@RequestParam Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by email", description = "Let the user search an user with its email")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by phone number", description = "Let the user search an user with its phone number")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<UserDTO> getByPhoneNumber(@RequestParam String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by job title", description = "Let the user search an user with its job title")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/by-job-title/{jobTitle}")
    public ResponseEntity<Page<UserDTO>> getByJobTitle(
            @RequestParam String jobTitle,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<User> users = userService.findByJobTitle(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)),
                jobTitle
        );

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<UserDTO> userDTOPage = users.map(UserDTO::fromUser);
        return ResponseEntity.ok(userDTOPage);
    }

    @Operation(summary = "Listing all available users", description = "Use to list all available users")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/available")
    public ResponseEntity<Page<UserDTO>> getAvailableUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<User> users = userService.findAvailableUsers(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<UserDTO> userDTOPage = users.map(UserDTO::fromUser);
        return ResponseEntity.ok(userDTOPage);
    }

    @Operation(summary = "Setting a user as available", description = "Use to set a user as available")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PutMapping("/users/{id}/available")
    public ResponseEntity<Void> setUserAvailable(@PathVariable Long id) {
        boolean updated = userService.setUserAvailable(id);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}
