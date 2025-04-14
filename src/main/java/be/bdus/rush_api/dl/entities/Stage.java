package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.persistence.*;
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
}
