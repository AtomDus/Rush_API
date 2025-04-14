package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StageRepository extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {
}
