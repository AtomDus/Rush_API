package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
@Getter
@Entity
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
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
    @ManyToOne
    private Project project;

    public Stage(String name, String description, LocalDate startingDate, LocalDate finishingDate, StageStatus status, User responsable) {
        this.name = name;
        this.description = description;
        this.startingDate = startingDate;
        this.finishingDate = finishingDate;
        this.status = status;
        this.responsable = responsable;
    }
}
