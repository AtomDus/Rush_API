package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.EmployeStatus;
import be.bdus.rush_api.dl.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor@AllArgsConstructor
@EqualsAndHashCode@ToString
@Getter
@Entity(name = "user_")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String lastname;

    @Setter
    @Column(nullable = false)
    private String firstname;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Setter
    @Column(nullable = false)
    private String jobTitle;

    @Setter
    @OneToMany
    private List<ProductionCompany> productionCompanies;

    @Setter
    @OneToMany
    private List<Project> projects;

    @Setter
    @Column(nullable = false)
    private boolean available = true;

    @Setter
    @Column(nullable = false)
    private EmployeStatus status = EmployeStatus.ACTIVE;

    @Setter
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User(String firstname, String lastname, String email, String phoneNumber, String jobTitle, boolean isAvailable, EmployeStatus status, String password) {
        this(firstname, lastname, email, password);
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.available = isAvailable;
        this.status = status;
    }

    public User(String username, String firstname, String lastname, String email, String phoneNumber,  String jobTitle, boolean isAvailable,  EmployeStatus status,  String password) {
        this(firstname, lastname, email, password);
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.available = isAvailable;
        this.status = status;
    }

    public User(String username, String firstname, String lastname, String email, String password) {
        this(firstname, lastname, email, password);
        this.username = username;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public void setFullName(String firstName, String lastName) {
        this.firstname = firstName;
        this.lastname = lastName;
    }

}
