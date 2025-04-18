package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.api.models.stage.dtos.StageCreationDTO;
import be.bdus.rush_api.api.models.stage.forms.StageCreationForm;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.StageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StageServiceImplTest {
    @Mock
    private StageRepository stageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private StageServiceImpl stageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Stage stage = new Stage();
        when(stageRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(stage)));

        Page<Stage> result = stageService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        verify(stageRepository).findAll(any(PageRequest.class));
    }

    @Test
    void testFindById_Found() {
        Stage mockStage = mock(Stage.class);
        when(stageRepository.findById(1L)).thenReturn(Optional.of(mockStage));

        Stage result = stageService.findById(1L);

        assertNotNull(result);
        assertSame(mockStage, result);
    }

    @Test
    void testFindById_NotFound() {
        when(stageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> stageService.findById(1L));
    }

    @Test
    void testDelete() {
        stageService.delete(1L);
        verify(stageRepository).deleteById(1L);
    }

    @Test
    void testSaveFromForm() {
        StageCreationForm form = new StageCreationForm(
                "Stage Name",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                StageStatus.OPEN,
                "user@example.com"
        );

        User responsable = new User();

        when(userRepository.findByEmail(form.responsableEmail())).thenReturn(Optional.of(responsable));
        when(stageRepository.save(any(Stage.class))).thenAnswer(i -> i.getArgument(0));

        Stage result = stageService.saveFromForm(form);

        assertNotNull(result);
        assertEquals("Stage Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals(form.startingDate(), result.getStartingDate());
        assertEquals(form.finishingDate(), result.getFinishingDate());
        assertEquals(StageStatus.OPEN, result.getStatus());
        assertSame(responsable, result.getResponsable());

        verify(stageRepository).save(any(Stage.class));
    }

    @Test
    void testSaveFromForm_ResponsableNotFound() {
        StageCreationForm form = new StageCreationForm(
                "Stage Name",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                StageStatus.OPEN,
                "nonexistent@example.com"
        );

        when(userRepository.findByEmail(form.responsableEmail())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> stageService.saveFromForm(form));
    }

    @Test
    void testUpdateFromForm() {
        StageCreationForm form = new StageCreationForm(
                "Stage Updated",
                "Updated description",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                StageStatus.CLOSED,
                "user@example.com"
        );

        Stage existingStage = new Stage();
        User responsable = new User();

        when(stageRepository.findById(1L)).thenReturn(Optional.of(existingStage));
        when(userRepository.findByEmail(form.responsableEmail())).thenReturn(Optional.of(responsable));
        when(stageRepository.save(any(Stage.class))).thenAnswer(i -> i.getArgument(0));

        Stage updated = stageService.updateFromForm(form, 1L);

        assertEquals("Stage Updated", updated.getName());
        assertEquals("Updated description", updated.getDescription());
        assertEquals(StageStatus.CLOSED, updated.getStatus());
        assertSame(responsable, updated.getResponsable());
    }

    @Test
    void testUpdateFromForm_StageNotFound() {
        StageCreationForm form = new StageCreationForm(
                "Stage Updated",
                "Updated description",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                StageStatus.CLOSED,
                "user@example.com"
        );

        when(stageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> stageService.updateFromForm(form, 1L));
    }

    @Test
    void testUpdateFromForm_ResponsableNotFound() {
        StageCreationForm form = new StageCreationForm(
                "Stage Updated",
                "Updated description",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                StageStatus.CLOSED,
                "user@example.com"
        );

        when(stageRepository.findById(1L)).thenReturn(Optional.of(new Stage()));
        when(userRepository.findByEmail(form.responsableEmail())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> stageService.updateFromForm(form, 1L));
    }

    @Test
    public void testValidateStageDates_withValidDates() {
        Project project = mock(Project.class);
        Stage stage = mock(Stage.class);

        LocalDate projectStart = LocalDate.of(2025, 1, 1);
        LocalDate projectEnd = LocalDate.of(2025, 12, 31);
        when(project.getStartingDate()).thenReturn(projectStart);
        when(project.getFinishingDate()).thenReturn(projectEnd);

        LocalDate stageStart = LocalDate.of(2025, 6, 1);
        LocalDate stageEnd = LocalDate.of(2025, 6, 15);
        when(stage.getStartingDate()).thenReturn(stageStart);
        when(stage.getFinishingDate()).thenReturn(stageEnd);

        assertDoesNotThrow(() -> stageService.validateStageDates(stage, project));
    }

    @Test
    public void testValidateStageDates_withStageDatesOutOfRange() {
        Project project = mock(Project.class);
        Stage stage = mock(Stage.class);

        LocalDate projectStart = LocalDate.of(2025, 1, 1);
        LocalDate projectEnd = LocalDate.of(2025, 12, 31);
        when(project.getStartingDate()).thenReturn(projectStart);
        when(project.getFinishingDate()).thenReturn(projectEnd);

        LocalDate stageStart = LocalDate.of(2026, 1, 1);
        LocalDate stageEnd = LocalDate.of(2026, 1, 15);
        when(stage.getStartingDate()).thenReturn(stageStart);
        when(stage.getFinishingDate()).thenReturn(stageEnd);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> stageService.validateStageDates(stage, project));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Stage dates must be within project range", exception.getReason());
    }

    @Test
    public void testValidateStageDates_withStageStartAfterEnd() {
        Project project = mock(Project.class);
        Stage stage = mock(Stage.class);

        LocalDate projectStart = LocalDate.of(2025, 1, 1);
        LocalDate projectEnd = LocalDate.of(2025, 12, 31);
        when(project.getStartingDate()).thenReturn(projectStart);
        when(project.getFinishingDate()).thenReturn(projectEnd);

        LocalDate stageStart = LocalDate.of(2025, 6, 15);
        LocalDate stageEnd = LocalDate.of(2025, 6, 1);
        when(stage.getStartingDate()).thenReturn(stageStart);
        when(stage.getFinishingDate()).thenReturn(stageEnd);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> stageService.validateStageDates(stage, project));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Stage starting date cannot be after finishing date", exception.getReason());
    }

}