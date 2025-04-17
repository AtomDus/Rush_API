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
        // ARRANGE - Préparer les données
        StageCreationForm form = new StageCreationForm(
                "Stage Name",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                StageStatus.OPEN,
                "user@example.com",
                "Project Name"
        );

        User user = new User();
        Project project = new Project();

        when(userRepository.findByEmail(form.responsableEmail())).thenReturn(Optional.of(user));
        when(projectRepository.findByName(form.projectName())).thenReturn(Optional.of(project));
        when(stageRepository.save(any(Stage.class))).thenAnswer(i -> i.getArgument(0));

        // ACT - Exécuter la méthode à tester
        Stage result = stageService.saveFromForm(form);

        // ASSERT - Vérifier les résultats
        assertNotNull(result);
        assertEquals("Stage Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals(form.startingDate(), result.getStartingDate());
        assertEquals(form.finishingDate(), result.getFinishingDate());
        assertEquals(StageStatus.OPEN, result.getStatus());
        assertSame(user, result.getResponsable());
        assertSame(project, result.getProject());

        // Vérifie que le repository a bien été appelé
        verify(stageRepository).save(any(Stage.class));
    }

    @Test
    void testUpdate() {
        StageCreationDTO dto = new StageCreationDTO(1L,
                "Stage Updated", "Desc", LocalDate.now(), LocalDate.now().plusDays(1),
                StageStatus.CLOSED.CLOSED, "user@example.com", "Project Name"
        );

        Stage existing = new Stage();
        User user = new User();
        Project project = new Project();

        when(stageRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail(dto.responsableEmail())).thenReturn(Optional.of(user));
        when(projectRepository.findByName(dto.projectName())).thenReturn(Optional.of(project));
        when(stageRepository.save(any(Stage.class))).thenAnswer(i -> i.getArgument(0));

        Stage updated = stageService.update(dto, 1L);

        assertEquals("Stage Updated", updated.getName());
    }
}