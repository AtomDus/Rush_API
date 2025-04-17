package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.bll.services.ProjectService;
import be.bdus.rush_api.dal.repositories.PCompanyRepository;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.ProductionCompany;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.dl.enums.UserRole;
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
    private final UserRepository userRepository;
    private final PCompanyRepository productionRepository;

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

    @Override
    public Project updateProjectStatus(Long id) {
        Project project = findById(id);
        if (project.getFinishingDate().isBefore(LocalDate.now()) && !project.getStatus().equals("Completed")) {
            project.setStatus(StageStatus.CLOSED);
            projectRepository.save(project);
        }
        return project;
    }

    @Override
    public Project saveFromForm(ProjectCreationForm projectForm) {
        User responsable = userRepository.findByEmail(projectForm.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));

        ProductionCompany productionCompany = productionRepository.findByName(projectForm.productionCompanyName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production company not found"));

        Project project = new Project();
        project.setName(projectForm.name());
        project.setDescription(projectForm.description());
        project.setStartingDate(projectForm.startingDate());
        project.setFinishingDate(projectForm.finishingDate());
        project.setStatus(projectForm.status());
        project.setResponsable(responsable);
        project.setProductionCompany(productionCompany);
        project.setBudget(projectForm.budget());
        project.setDuration(projectForm.duration());

        return projectRepository.save(project);
    }

    @Override
    public Project updateFromForm(ProjectCreationForm form, Long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        existingProject.setName(form.name());
        existingProject.setDescription(form.description());
        existingProject.setStartingDate(form.startingDate());
        existingProject.setFinishingDate(form.finishingDate());
        existingProject.setStatus(form.status());

        User responsable = userRepository.findByEmail(form.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));
        existingProject.setResponsable(responsable);

        ProductionCompany productionCompany = productionRepository.findByName(form.productionCompanyName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production company not found"));
        existingProject.setProductionCompany(productionCompany);

        return projectRepository.save(existingProject);
    }

    @Override
    public Project addStageToProject(Long projectId, StageCreationForm stageForm) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        User responsable = userRepository.findByEmail(stageForm.responsableEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable not found"));

        Stage stage = new Stage();
        stage.setName(stageForm.name());
        stage.setDescription(stageForm.description());
        stage.setStartingDate(stageForm.startingDate());
        stage.setFinishingDate(stageForm.finishingDate());
        stage.setStatus(stageForm.status());
        stage.setResponsable(responsable);
        stage.setProject(project);

        project.getStages().add(stage);

        return projectRepository.save(project);
    }

    @Override
    public Project removeStageFromProject(Long projectId, Long stageId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.getStages().removeIf(stage -> stage.getId().equals(stageId));

        return projectRepository.save(project);
    }

    @Override
    public Project addEmployeToProject(Long projectId, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstname("Temporary");
            user.setLastname("User");
            user.setRole(UserRole.NOT_USER);
            user.setAvailable(false);
            user = userRepository.save(user);
        } else {
            user.setRole(UserRole.STAFF);
            userRepository.save(user);
        }

        if (!project.getEmployes().contains(user)) {
            project.getEmployes().add(user);
        }

        return projectRepository.save(project);
    }
}
