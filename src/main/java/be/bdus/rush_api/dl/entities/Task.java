package be.bdus.rush_api.dl.entities;

import be.bdus.rush_api.dl.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor@AllArgsConstructor
@ToString@EqualsAndHashCode
@Getter
@Entity
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Setter
    @Column
    String name;

    @Setter
    @Column
    String description;

    @Setter
    @Column
    TaskStatus status;

    @Setter
    @Column
    LocalDate dueDate;

    @Setter
    @Column
    LocalDate completionDate;

    @Setter
    @ManyToOne
    Stage Stage;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
