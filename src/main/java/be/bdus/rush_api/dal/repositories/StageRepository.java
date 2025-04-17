package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {
    List<Stage> findByFinishingDate(LocalDate date);

    List<Stage> findByStartingDate(LocalDate date);

    Optional<Stage> findByName(String name);
}
