package be.bdus.rush_api.dal.repositories;

import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.enums.StageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);

    Page<Project> findByStatus( Pageable pageable, StageStatus status);

    Page<Project> findByResponsableId(Pageable pageable, Long Responsable_id);

    Page<Project> findByStatusAndResponsableId(Pageable pageable, StageStatus status, Long responsableId);
}
