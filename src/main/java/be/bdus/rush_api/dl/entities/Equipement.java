package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.EquipementCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
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
    private LocationCompany owner;

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
    private LocalDate dateAcquisition;

    @Setter
    @Column
    private LocalDate dateLastRevision;

    public Equipement(String name, LocationCompany owner, String description, String model, String serialNumber, String type, EquipementCondition condition, int stock, String stockLocation, LocalDate acquisitionDate, LocalDate lastRevision) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.model = model;
        this.serialNumber = serialNumber;
        this.type = type;
        this.condition = condition;
        this.stock = stock;
        this.stockagePlace = stockLocation;
        this.dateAcquisition = acquisitionDate;
        this.dateLastRevision = lastRevision;
    }


}
