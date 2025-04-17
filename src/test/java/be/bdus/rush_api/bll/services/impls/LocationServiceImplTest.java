package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dal.repositories.RCompanyRepository;
import be.bdus.rush_api.dl.entities.RentingCompany;
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

    private RCompanyRepository locationRepository;
    private RentingServiceImpl locationService;

    @BeforeEach
    void setUp() {
        locationRepository = mock(RCompanyRepository.class);
        locationService = new RentingServiceImpl(locationRepository);
    }

    @Test
    void testFindAll() {
        Page<RentingCompany> page = new PageImpl<>(List.of(new RentingCompany()));
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<RentingCompany> result = locationService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(locationRepository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Found() {
        RentingCompany location = new RentingCompany();
        ReflectionTestUtils.setField(location, "id", 1L);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        RentingCompany result = locationService.findById(1L);

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
        RentingCompany location = new RentingCompany();
        when(locationRepository.save(location)).thenReturn(location);

        RentingCompany saved = locationService.save(location);

        assertNotNull(saved);
        verify(locationRepository).save(location);
    }

    @Test
    void testUpdate_Found() {
        RentingCompany existing = new RentingCompany();
        ReflectionTestUtils.setField(existing, "id", 1L);
        RentingCompany updated = new RentingCompany();
        updated.setName("New Name");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(any())).thenReturn(existing);

        RentingCompany result = locationService.update(updated, 1L);

        assertEquals("New Name", result.getName());
        verify(locationRepository).findById(1L);
        verify(locationRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        RentingCompany updated = new RentingCompany();
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
        RentingCompany location = new RentingCompany();
        location.setName("Studio A");

        when(locationRepository.findByName("Studio A")).thenReturn(Optional.of(location));

        Optional<RentingCompany> result = locationService.findByName("Studio A");

        assertTrue(result.isPresent());
        assertEquals("Studio A", result.get().getName());
        verify(locationRepository).findByName("Studio A");
    }

    @Test
    void testFindByProjectsId() {
        Page<RentingCompany> page = new PageImpl<>(List.of(new RentingCompany()));
        when(locationRepository.findByProjectsId(eq(10L), any(Pageable.class))).thenReturn(page);

        Page<RentingCompany> result = locationService.findByProjectsId(Pageable.unpaged(), 10L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(locationRepository).findByProjectsId(eq(10L), any(Pageable.class));
    }
}