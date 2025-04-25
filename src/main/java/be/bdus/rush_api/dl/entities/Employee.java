package be.bdus.rush_api.dl.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String lastname;

    @Setter
    @Column(nullable = false)
    private String firstname;

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
    @ManyToMany()
    private List<Project> projects;

    @Setter
    @Column(nullable = false)
    private boolean available = true;

    public Employee(String firstname, String lastname, String email, String phoneNumber, String jobTitle, boolean isAvailable) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.available = isAvailable;
    }

    public Employee(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
}
