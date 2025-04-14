package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Setter
    @Column(nullable = false, length = 200)
    private String description;

    @Setter
    @Column
    private Date startingDate;

    @Setter
    @Column
    private Date finishingDate;

    @Setter
    @Column(nullable = false)
    private StageStatus status;

    @Setter
    @OneToOne
    private User responsable;

    public Stage(String name, String description, Date startingDate, Date finishingDate, StageStatus status, User responsable) {
        this.name = name;
        this.description = description;
        this.startingDate = startingDate;
        this.finishingDate = finishingDate;
        this.status = status;
        this.responsable = responsable;
    }
}
