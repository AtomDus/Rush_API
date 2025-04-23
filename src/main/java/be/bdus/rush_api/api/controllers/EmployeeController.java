package be.bdus.rush_api.api.controllers;


import be.bdus.rush_api.api.models.employee.dtos.EmployeeDTO;
import be.bdus.rush_api.bll.services.EmployeeService;
import be.bdus.rush_api.dl.entities.Employee;
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
@RequestMapping("/employee")

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "employees", description = "Endpoints use for employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Listing users by id", description = "Let the user search an user with its id")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@RequestParam Long id) {
        return employeeService.findById(id)
                .map(employee -> ResponseEntity.ok(EmployeeDTO.fromEmployee(employee)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by email", description = "Let the user search an user with its email")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDTO> getByEmail(@RequestParam String email) {
        return employeeService.findByEmail(email)
                .map(employee -> ResponseEntity.ok(EmployeeDTO.fromEmployee(employee)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by phone number", description = "Let the user search an user with its phone number")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<EmployeeDTO> getByPhoneNumber(@RequestParam String phoneNumber) {
        return employeeService.findByPhoneNumber(phoneNumber)
                .map(employee -> ResponseEntity.ok(EmployeeDTO.fromEmployee(employee)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listing users by job title", description = "Let the user search an user with its job title")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/by-job-title/{jobTitle}")
    public ResponseEntity<Page<EmployeeDTO>> getByJobTitle(
            @RequestParam String jobTitle,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Employee> employees = employeeService.findByJobTitle(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)),
                jobTitle
        );

        if (employees.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<EmployeeDTO> employeeDTOS = employees.map(EmployeeDTO::fromEmployee);
        return ResponseEntity.ok(employeeDTOS);
    }

    @Operation(summary = "Listing all available users", description = "Use to list all available users")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @GetMapping("/available")
    public ResponseEntity<Page<EmployeeDTO>> getAvailableUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Employee> employees = employeeService.findAvailableUsers(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort))
        );
        if (employees.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<EmployeeDTO> employeeDTOS = employees.map(EmployeeDTO::fromEmployee);
        return ResponseEntity.ok(employeeDTOS);
    }

    @Operation(summary = "Setting a user as available", description = "Use to set a user as available")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('STAFF')")
    @PutMapping("/users/{id}/available")
    public ResponseEntity<Void> setUserAvailable(@PathVariable Long id) {
        boolean updated = employeeService.setUserAvailable(id);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
