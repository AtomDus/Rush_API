package be.bdus.rush_api.api.models.stage.forms;

import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record StageForm(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        Date startingDate,

        @NotNull
        Date finishingDate,

        @NotNull
        StageStatus status,

        @NotNull
        User responsable
) {
    public  Stage toStage() {
        Stage stage = new Stage(name, description, startingDate, finishingDate, status, responsable);
        return stage;
    }

}
