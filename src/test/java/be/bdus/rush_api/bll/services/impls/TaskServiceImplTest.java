package be.bdus.rush_api.bll.services.impls;

import static org.junit.jupiter.api.Assertions.*;

import be.bdus.rush_api.api.models.task.forms.TaskForm;
import be.bdus.rush_api.dal.repositories.StageRepository;
import be.bdus.rush_api.dal.repositories.TaskRepository;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.Task;
import be.bdus.rush_api.dl.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private StageRepository stageRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Page<Task> result = taskService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAll(pageable);
    }

    @Test
    void testFindById_Found() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(task, result.get());
        verify(taskRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(1L);

        assertTrue(result.isEmpty());
        verify(taskRepository).findById(1L);
    }

    @Test
    void testDelete() {
        taskService.delete(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testSaveFromForm_success() {
        // Given
        TaskForm form = new TaskForm(
                "Task Name",
                "Description",
                TaskStatus.IN_PROGRESS,
                LocalDate.now(),
                "Stage 1"
        );

        Stage stage = new Stage();
        stage.setName("Stage 1");
        stage.setTasks(new ArrayList<>());

        when(stageRepository.findByName("Stage 1")).thenReturn(Optional.of(stage));
        when(stageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        Stage result = taskService.saveFromForm(form);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTasks().size());
        Task savedTask = result.getTasks().get(0);
        assertEquals("Task Name", savedTask.getName());
        assertEquals("Description", savedTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, savedTask.getStatus());

        verify(stageRepository).findByName("Stage 1");
        verify(stageRepository).save(stage);
    }

    @Test
    void testUpdateFromForm_changeStage() {
        // Given
        Stage oldStage = new Stage();
        oldStage.setName("Old Stage");
        oldStage.setTasks(new ArrayList<>());

        Task existingTask = new Task();
        existingTask.setName("Old Task");
        existingTask.setStage(oldStage);
        oldStage.getTasks().add(existingTask);

        Stage newStage = new Stage();
        newStage.setName("New Stage");
        newStage.setTasks(new ArrayList<>());

        TaskForm form = new TaskForm(
                "Updated Task",
                "Updated Description",
                TaskStatus.IN_PROGRESS,
                LocalDate.now(),
                "New Stage"
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(stageRepository.findByName("New Stage")).thenReturn(Optional.of(newStage));
        when(stageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        Stage result = taskService.updateFromForm(form, 1L);

        // Then
        assertEquals("New Stage", result.getName());
        assertTrue(result.getTasks().contains(existingTask));
        assertFalse(oldStage.getTasks().contains(existingTask));
        assertEquals("Updated Task", existingTask.getName());

        verify(stageRepository).save(oldStage);
        verify(stageRepository).save(newStage);
    }

    @Test
    void testUpdateFromForm_sameStage() {
        // Given
        Stage stage = new Stage();
        stage.setName("Same Stage");
        stage.setTasks(new ArrayList<>());

        Task existingTask = new Task();
        existingTask.setName("Old Task");
        existingTask.setStage(stage);
        stage.getTasks().add(existingTask);

        TaskForm form = new TaskForm(
                "New Task",
                "New Description",
                TaskStatus.IN_PROGRESS,
                LocalDate.now(),
                "Same Stage"
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        Stage result = taskService.updateFromForm(form, 1L);

        // Then
        assertEquals("Same Stage", result.getName());
        assertEquals("New Task", existingTask.getName());
        assertEquals(TaskStatus.IN_PROGRESS, existingTask.getStatus());

        verify(taskRepository).save(existingTask);
        verify(stageRepository, never()).save(any());
    }
}
