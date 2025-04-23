package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.employee.forms.EmployeeForm;
import be.bdus.rush_api.api.models.project.forms.ProjectCreationForm;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.dal.repositories.EmployeeRepository;
import be.bdus.rush_api.dal.repositories.PCompanyRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.*;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dl.enums.StageStatus;
import be.bdus.rush_api.dl.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Mock
    private EmployeeRepository employeeRepository;

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
    void shouldAddExistingEmployeeToProject() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmail("existing@employee.com");

        Project project = new Project();
        project.setEmployes(new ArrayList<>());

        EmployeeForm form = new EmployeeForm(
                "existing@employee.com", "John", "Doe", "0123456789", "Engineer", true
        );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findByEmail(form.email())).thenReturn(Optional.of(existingEmployee));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.addEmployeToProject(1L, form);

        assertEquals(1, result.getEmployes().size());
        assertTrue(result.getEmployes().contains(existingEmployee));
    }

    @Test
    void shouldCreateAndAddNewEmployeeToProject() {
        EmployeeForm form = new EmployeeForm(
                "new@employee.com", "Alice", "Smith", "0987654321", "Manager", true
        );

        Project project = new Project();
        project.setEmployes(new ArrayList<>());

        Employee savedEmployee = new Employee();
        savedEmployee.setEmail(form.email());
        savedEmployee.setFirstname(form.firstname());
        savedEmployee.setLastname(form.lastname());
        savedEmployee.setPhoneNumber(form.phoneNumber());
        savedEmployee.setJobTitle(form.jobTitle());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findByEmail(form.email())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.addEmployeToProject(1L, form);

        assertEquals(1, result.getEmployes().size());
        assertEquals(form.email(), result.getEmployes().get(0).getEmail());
        assertEquals(form.firstname(), result.getEmployes().get(0).getFirstname());
    }

    @Test
    void shouldThrowIfProjectNotFound() {
        EmployeeForm form = new EmployeeForm(
                "nobody@ghost.com", "Ghost", "Nobody", "0000000000", "Phantom", true
        );

        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> projectService.addEmployeToProject(999L, form)
        );

        assertEquals("404 NOT_FOUND \"Project not found\"", exception.getMessage());
    }

    @Test
    public void testGetPendingProjects() {
        Pageable pageable = PageRequest.of(0, 10);

        Project project1 = new Project();
        project1.setStatus(StageStatus.PENDING);

        Project project2 = new Project();
        project2.setStatus(StageStatus.PENDING);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatus(pageable, StageStatus.PENDING)).thenReturn(projectPage);

        Page<Project> result = projectService.getPendingProjects(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.PENDING, result.getContent().get(0).getStatus());
    }

    @Test
    public void testGetOpenProjects() {
        Pageable pageable = PageRequest.of(0, 10);

        Project project1 = new Project();
        project1.setStatus(StageStatus.OPEN);

        Project project2 = new Project();
        project2.setStatus(StageStatus.OPEN);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatus(pageable, StageStatus.OPEN)).thenReturn(projectPage);

        Page<Project> result = projectService.getOpenProjects(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.OPEN, result.getContent().get(0).getStatus());
    }

    @Test
    public void testGetClosedProjects() {
        Pageable pageable = PageRequest.of(0, 10);

        Project project1 = new Project();
        project1.setStatus(StageStatus.CLOSED);

        Project project2 = new Project();
        project2.setStatus(StageStatus.CLOSED);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatus(pageable, StageStatus.CLOSED)).thenReturn(projectPage);

        Page<Project> result = projectService.getClosedProjects(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.CLOSED, result.getContent().get(0).getStatus());
    }

    @Test
    public void testGetProjectsByResponsable() {
        Pageable pageable = PageRequest.of(0, 10);
        Long responsableId = 1L;

        User responsable = new User();
        ReflectionTestUtils.setField(responsable, "id", responsableId);

        Project project1 = new Project();
        project1.setResponsable(responsable);

        Project project2 = new Project();
        project2.setResponsable(responsable);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByResponsableId(pageable, responsableId)).thenReturn(projectPage);

        Page<Project> result = projectService.getProjectsByResponsableId(pageable, responsableId);

        assertEquals(2, result.getTotalElements());
        assertEquals(responsableId, result.getContent().get(0).getResponsable().getId());
    }

    @Test
    public void testGetPendingProjectsByResponsable() {
        Pageable pageable = PageRequest.of(0, 10);
        Long responsableId = 1L;

        User responsable = new User();
        ReflectionTestUtils.setField(responsable, "id", responsableId);

        Project project1 = new Project();
        project1.setStatus(StageStatus.PENDING);
        project1.setResponsable(responsable);

        Project project2 = new Project();
        project2.setStatus(StageStatus.PENDING);
        project2.setResponsable(responsable);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatusAndResponsableId(pageable, StageStatus.PENDING, responsableId))
                .thenReturn(projectPage);

        Page<Project> result = projectService.getPendingProjectsByResponsableId(pageable, responsableId);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.PENDING, result.getContent().get(0).getStatus());
        assertEquals(responsableId, result.getContent().get(0).getResponsable().getId());
    }

    @Test
    public void testGetOpenProjectsByResponsable() {
        Pageable pageable = PageRequest.of(0, 10);
        Long responsableId = 1L;

        User responsable = new User();
        ReflectionTestUtils.setField(responsable, "id", responsableId);

        Project project1 = new Project();
        project1.setStatus(StageStatus.OPEN);
        project1.setResponsable(responsable);

        Project project2 = new Project();
        project2.setStatus(StageStatus.OPEN);
        project2.setResponsable(responsable);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatusAndResponsableId(pageable, StageStatus.OPEN, responsableId))
                .thenReturn(projectPage);

        Page<Project> result = projectService.getOpenProjectsByResponsableId(pageable, responsableId);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.OPEN, result.getContent().get(0).getStatus());
        assertEquals(responsableId, result.getContent().get(0).getResponsable().getId());
    }

    @Test
    public void testGetClosedProjectsByResponsable() {
        Pageable pageable = PageRequest.of(0, 10);
        Long responsableId = 1L;

        User responsable = new User();
        ReflectionTestUtils.setField(responsable, "id", responsableId);

        Project project1 = new Project();
        project1.setStatus(StageStatus.CLOSED);
        project1.setResponsable(responsable);

        Project project2 = new Project();
        project2.setStatus(StageStatus.CLOSED);
        project2.setResponsable(responsable);

        Page<Project> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        when(projectRepository.findByStatusAndResponsableId(pageable, StageStatus.CLOSED, responsableId))
                .thenReturn(projectPage);

        Page<Project> result = projectService.getClosedProjectsByResponsableId(pageable, responsableId);

        assertEquals(2, result.getTotalElements());
        assertEquals(StageStatus.CLOSED, result.getContent().get(0).getStatus());
        assertEquals(responsableId, result.getContent().get(0).getResponsable().getId());
    }

}
