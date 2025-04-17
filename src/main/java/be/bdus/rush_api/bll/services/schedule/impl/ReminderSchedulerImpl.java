package be.bdus.rush_api.bll.services.schedule.impl;

import be.bdus.rush_api.bll.services.mails.EmailService;
import be.bdus.rush_api.bll.services.schedule.ReminderScheduler;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReminderSchedulerImpl implements ReminderScheduler {
    private final StageRepository stageRepository;
    private final EmailService mailService;

    @Scheduled(cron = "0 0 8 ? * MON-FRI")
    public void sendStageReminders() {
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now.plusDays(2);

        List<Stage> upcomingStages = stageRepository.findByFinishingDate(targetDate);

        for (Stage stage : upcomingStages) {
            User responsable = stage.getResponsable();

            mailService.sendTemplateEmail(
                    responsable.getEmail(),
                    "Rappel : votre stage " + stage.getName() + " approche de sa fin",
                    "emails/event-reminder",
                    Map.of(
                            "email", responsable.getEmail(),
                            "stageName", stage.getName(),
                            "finishingDate", stage.getFinishingDate().toString(),
                            "stageResponsable", responsable.getFullName()
                    )
            );
        }
    }
}
