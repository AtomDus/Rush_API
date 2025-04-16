package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dl.entities.Project;
import be.bdus.rush_api.dal.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    private ProjectRepository projectRepository;
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectServiceImpl(projectRepository);
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
}
