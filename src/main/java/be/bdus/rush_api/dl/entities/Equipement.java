package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.EquipementCondition;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
@Getter
@Entity
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @ManyToOne
    private LocationCompany Owner;

    @Setter
    @Column(nullable = false, length = 200)
    private String description;

    @Setter
    @Column
    private String model;

    @Setter
    @Column(unique = true)
    private String serialNumber;

    @Setter
    @Column(nullable = false)
    private String type;

    @Setter
    @Column(nullable = false)
    private EquipementCondition condition;

    @Setter
    @Column
    private int stock;

//    @Setter
//    @Column
//    private int assurance;

    @Setter
    @Column(nullable = false)
    private String stockagePlace;

    @Setter
    @Column
    private Date dateAcquisition;

    @Setter
    @Column
    private Date dateLastRevision;
}
