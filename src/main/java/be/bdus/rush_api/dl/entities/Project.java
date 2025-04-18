package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
@Getter
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Setter
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Setter
    @Column(nullable = false, length = 200)
    private String description;

    @Setter
    @Column
    private LocalDate startingDate;

    @Setter
    @Column
    private LocalDate finishingDate;

    @Setter
    @Column(nullable = false)
    private StageStatus status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private User responsable;

    @Setter
    @ManyToMany
    private List<Employee> employes;

    @Setter
    @ManyToOne
    private ProductionCompany productionCompany;

    @Setter
    @ManyToMany
    private List<Equipement> equipements;

    @Setter
    @ManyToMany
    private List<RentingCompany> locationCompanies;

    @Setter
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stage> stages = new ArrayList<>();

    @Setter
    @Column
    private int nbOfStages;

    @Setter
    @Column
    private double pourcentageDone;

    @Setter
    @Column
    private int duration;

    @Setter
    @Column
    private int budget;

    @Setter
    @Column
    private String place;

    public Project(String name, String description, LocalDate startingDate, LocalDate finishingDate, StageStatus status, User responsable,
                   List<Employee> employes, ProductionCompany productionCompany, List<Equipement> equipements, List<Stage> stages,
                   int nbOfStages, double pourcentageDone, int duration, int budget, String place) {
        this.name = name;
        this.description = description;
        this.startingDate = startingDate;
        this.finishingDate = finishingDate;
        this.status = status;
        this.responsable = responsable;
        this.employes = employes;
        this.productionCompany = productionCompany;
        this.equipements = equipements;
        this.stages = stages;
        this.nbOfStages = nbOfStages;
        this.pourcentageDone = pourcentageDone;
        this.duration = duration;
        this.budget = budget;
        this.place = place;
    }

}
