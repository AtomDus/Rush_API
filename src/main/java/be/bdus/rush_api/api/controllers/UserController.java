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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

@Tag(name = "users", description = "Endpoints use for users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Listing users by id", description = "Let the user search an user with its id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@RequestParam Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by email", description = "Let the user search an user with its email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by phone number", description = "Let the user search an user with its phone number")
    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<UserDTO> getByPhoneNumber(@RequestParam String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber)
                .map(user -> ResponseEntity.ok(UserDTO.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by job title", description = "Let the user search an user with its job title")
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

}
