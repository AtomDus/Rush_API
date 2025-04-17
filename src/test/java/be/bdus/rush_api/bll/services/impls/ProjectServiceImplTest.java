package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.dal.repositories.PCompanyRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.ProductionCompany;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.dl.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PCompanyRepository productionRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Page<Project> projectPage = new PageImpl<>(Collections.singletonList(new Project()));
        when(projectRepository.findAll(any(Pageable.class))).thenReturn(projectPage);

        Page<Project> result = projectService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(projectRepository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Found() {
        Project project = new Project();
        ReflectionTestUtils.setField(project, "id", 1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(projectRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.findById(999L);
        });

        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository).findById(999L);
    }

    @Test
    void testSave() {
        Project project = new Project();
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.save(project);

        assertNotNull(result);
        verify(projectRepository).save(project);
    }

    @Test
    void testUpdate_Found() {
        Project existing = new Project();
        Project updated = new Project();
        updated.setName("New Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.update(updated, 1L);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.update(new Project(), 2L);
        });

        assertEquals("404 NOT_FOUND \"Company not found\"", exception.getMessage());
        verify(projectRepository).findById(2L);
    }

    @Test
    void testDelete() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.delete(1L);

        verify(projectRepository).deleteById(1L);
    }

    @Test
    void testUpdateProjectStatus_ShouldCloseProject() {
        Project project = new Project();
        project.setFinishingDate(LocalDate.now().minusDays(1));
        project.setStatus(StageStatus.OPEN);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updated = projectService.updateProjectStatus(1L);

        assertEquals(StageStatus.CLOSED, updated.getStatus());
    }

    @Test
    void testSaveFromForm_ShouldSaveProject() {
        ProjectCreationForm form = new ProjectCreationForm(
                "Project Name", "Description", LocalDate.now(), LocalDate.now().plusDays(10),
                StageStatus.OPEN, "user@example.com", "ProdCo", 30, 10000
        );

        User user = new User();
        ProductionCompany company = new ProductionCompany();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productionRepository.findByName("ProdCo")).thenReturn(Optional.of(company));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project saved = projectService.saveFromForm(form);

        assertEquals("Project Name", saved.getName());
        assertEquals(user, saved.getResponsable());
        assertEquals(company, saved.getProductionCompany());
    }

    @Test
    void testUpdateFromForm_ShouldUpdateProject() {
        Long projectId = 1L;
        Project existing = new Project();

        User user = new User();
        ProductionCompany company = new ProductionCompany();

        ProjectCreationForm form = new ProjectCreationForm(
                "Updated Project", "Updated Desc", LocalDate.now(), LocalDate.now().plusDays(5),
                StageStatus.CLOSED, "user@example.com", "ProdCo", 60, 20000
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productionRepository.findByName("ProdCo")).thenReturn(Optional.of(company));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project updated = projectService.updateFromForm(form, projectId);

        assertEquals("Updated Project", updated.getName());
        assertEquals(user, updated.getResponsable());
        assertEquals(company, updated.getProductionCompany());
    }

    @Test
    void testAddStageToProject_ShouldAddStage() {
        Long projectId = 1L;
        StageCreationForm form = new StageCreationForm(
                "Stage 1", "Description", LocalDate.now(), LocalDate.now().plusDays(5),
                StageStatus.OPEN, "responsable@example.com"
        );

        Project project = new Project();
        project.setStages(new ArrayList<>());

        User responsable = new User();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail("responsable@example.com")).thenReturn(Optional.of(responsable));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.addStageToProject(projectId, form);

        assertEquals(1, result.getStages().size());
        assertEquals("Stage 1", result.getStages().get(0).getName());
        verify(projectRepository).save(project);
    }

    @Test
    void testAddStageToProject_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        StageCreationForm form = new StageCreationForm(
                "Stage", "Desc", LocalDate.now(), LocalDate.now().plusDays(3),
                StageStatus.OPEN, "mail@mail.com"
        );

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.addStageToProject(1L, form);
        });

        assertEquals("404 NOT_FOUND \"Project not found\"", exception.getMessage());
    }

    @Test
    void testAddStageToProject_ResponsableNotFound() {
        Project project = new Project();
        project.setStages(new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail("missing@mail.com")).thenReturn(Optional.empty());

        StageCreationForm form = new StageCreationForm(
                "Stage", "Desc", LocalDate.now(), LocalDate.now().plusDays(3),
                StageStatus.OPEN, "missing@mail.com"
        );

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.addStageToProject(1L, form);
        });

        assertEquals("404 NOT_FOUND \"Responsable not found\"", exception.getMessage());
    }

    @Test
    void testRemoveStageFromProject_ShouldRemoveStage() {
        Long stageId = 42L;
        Stage stage = new Stage();
        ReflectionTestUtils.setField(stage, "id", stageId);

        Project project = new Project();
        project.setStages(new ArrayList<>(Collections.singletonList(stage)));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.removeStageFromProject(1L, stageId);

        assertTrue(result.getStages().isEmpty());
    }

    @Test
    void testRemoveStageFromProject_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.removeStageFromProject(1L, 99L);
        });

        assertEquals("404 NOT_FOUND \"Project not found\"", exception.getMessage());
    }

    @Test
    void testAddEmployeToProject_ShouldAddExistingUser() {
        String email = "existing@user.com";
        User user = new User();
        user.setEmail(email);
        user.setRole(UserRole.NOT_USER);

        Project project = new Project();
        project.setEmployes(new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.addEmployeToProject(1L, email);

        assertEquals(1, result.getEmployes().size());
        assertEquals(UserRole.STAFF, user.getRole());
    }

    @Test
    void testAddEmployeToProject_ShouldCreateTempUser() {
        String email = "new@user.com";

        Project project = new Project();
        project.setEmployes(new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.addEmployeToProject(1L, email);

        assertEquals(1, result.getEmployes().size());
        assertEquals(email, result.getEmployes().get(0).getEmail());
        assertEquals(UserRole.NOT_USER, result.getEmployes().get(0).getRole());
    }

    @Test
    void testAddEmployeToProject_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.addEmployeToProject(1L, "nobody@nowhere.com");
        });

        assertEquals("404 NOT_FOUND \"Project not found\"", exception.getMessage());
    }
}
