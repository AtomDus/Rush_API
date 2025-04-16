package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.ProjectService;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.enums.StageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project update(Project project, Long id) {
        Optional<Project> selectedProject = projectRepository.findById(id);
        if (selectedProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        Project updatedProject = selectedProject.get();
        updatedProject.setName(project.getName());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setResponsable(project.getResponsable());
        updatedProject.setProductionCompany(project.getProductionCompany());
        updatedProject.setEmployes(project.getEmployes());
        updatedProject.setEquipements(project.getEquipements());
        updatedProject.setStages(project.getStages());
        updatedProject.setNbOfStages(project.getNbOfStages());
        updatedProject.setPourcentageDone(project.getPourcentageDone());
        updatedProject.setDuration(project.getDuration());
        updatedProject.setBudget(project.getBudget());
        updatedProject.setPlace(project.getPlace());
        updatedProject.setStartingDate(project.getStartingDate());
        updatedProject.setFinishingDate(project.getFinishingDate());
        updatedProject.setStatus(project.getStatus());
        return projectRepository.save(updatedProject);
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public Project updateProjectStatus(Long id) {
        Project project = findById(id);
        if (project.getFinishingDate().isBefore(LocalDate.now()) && !project.getStatus().equals("Completed")) {
            project.setStatus(StageStatus.CLOSED);
            projectRepository.save(project);
        }
        return project;
    }
}
