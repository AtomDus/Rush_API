package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dal.repositories.LCompanyRepository;
import be.bdus.rush_api.dl.entities.LocationCompany;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationServiceImplTest {

    private LCompanyRepository locationRepository;
    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        locationRepository = mock(LCompanyRepository.class);
        locationService = new LocationServiceImpl(locationRepository);
    }

    @Test
    void testFindAll() {
        Page<LocationCompany> page = new PageImpl<>(List.of(new LocationCompany()));
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<LocationCompany> result = locationService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(locationRepository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Found() {
        LocationCompany location = new LocationCompany();
        ReflectionTestUtils.setField(location, "id", 1L);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        LocationCompany result = locationService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, ReflectionTestUtils.getField(result, "id"));
        verify(locationRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> locationService.findById(999L));
    }

    @Test
    void testSave() {
        LocationCompany location = new LocationCompany();
        when(locationRepository.save(location)).thenReturn(location);

        LocationCompany saved = locationService.save(location);

        assertNotNull(saved);
        verify(locationRepository).save(location);
    }

    @Test
    void testUpdate_Found() {
        LocationCompany existing = new LocationCompany();
        ReflectionTestUtils.setField(existing, "id", 1L);
        LocationCompany updated = new LocationCompany();
        updated.setName("New Name");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(any())).thenReturn(existing);

        LocationCompany result = locationService.update(updated, 1L);

        assertEquals("New Name", result.getName());
        verify(locationRepository).findById(1L);
        verify(locationRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        LocationCompany updated = new LocationCompany();
        when(locationRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> locationService.update(updated, 123L));
    }

    @Test
    void testDelete() {
        locationService.delete(1L);
        verify(locationRepository).deleteById(1L);
    }

    @Test
    void testFindByName() {
        LocationCompany location = new LocationCompany();
        location.setName("Studio A");

        when(locationRepository.findByName("Studio A")).thenReturn(Optional.of(location));

        Optional<LocationCompany> result = locationService.findByName("Studio A");

        assertTrue(result.isPresent());
        assertEquals("Studio A", result.get().getName());
        verify(locationRepository).findByName("Studio A");
    }

    @Test
    void testFindByProjectsId() {
        Page<LocationCompany> page = new PageImpl<>(List.of(new LocationCompany()));
        when(locationRepository.findByProjectsId(eq(10L), any(Pageable.class))).thenReturn(page);

        Page<LocationCompany> result = locationService.findByProjectsId(Pageable.unpaged(), 10L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(locationRepository).findByProjectsId(eq(10L), any(Pageable.class));
    }
}