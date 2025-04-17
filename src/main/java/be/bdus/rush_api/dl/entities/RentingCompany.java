package be.bdus.rush_api.dl.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
@Getter
@Entity
public class RentingCompany {

    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String address;

    @Setter
    @Column(nullable = false)
    private String zipCode;

    @Setter
    @Column(nullable = false)
    private String city;

    @Setter
    @Column(nullable = false)
    private String country;

    @Setter
    @Column(nullable = false)
    private String phoneNumber;

    @Setter
    @Column(nullable = false)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "location_company_project",
            joinColumns = @JoinColumn(name = "location_company_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;

    public RentingCompany(String name, String address, String zipCode, String city, String country, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
