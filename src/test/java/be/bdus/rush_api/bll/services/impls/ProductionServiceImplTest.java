package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.ProductionService;
import be.bdus.rush_api.dal.repositories.LCompanyRepository;
import be.bdus.rush_api.dal.repositories.PCompanyRepository;
import be.bdus.rush_api.dl.entities.LocationCompany;
import be.bdus.rush_api.dl.entities.ProductionCompany;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ProductionServiceImplTest {

    private PCompanyRepository pCompanyRepository;
    private ProductionService productionService;

    @BeforeEach
    void setUp() {
        pCompanyRepository = mock(PCompanyRepository.class);
        productionService = new ProductionServiceImpl(pCompanyRepository);
    }

    @Test
    void testFindAll() {
        Page<ProductionCompany> page = new PageImpl<>(List.of(new ProductionCompany()));
        when(pCompanyRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductionCompany> result = productionService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(pCompanyRepository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Found() {
        ProductionCompany location = new ProductionCompany();
        ReflectionTestUtils.setField(location, "id", 1L);
        when(pCompanyRepository.findById(1L)).thenReturn(Optional.of(location));

        ProductionCompany result = productionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, ReflectionTestUtils.getField(result, "id"));
        verify(pCompanyRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(pCompanyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productionService.findById(999L));
    }

    @Test
    void testSave() {
        ProductionCompany location = new ProductionCompany();
        when(pCompanyRepository.save(location)).thenReturn(location);

        ProductionCompany saved = productionService.save(location);

        assertNotNull(saved);
        verify(pCompanyRepository).save(location);
    }

    @Test
    void testUpdate_Found() {
        ProductionCompany existing = new ProductionCompany();
        ReflectionTestUtils.setField(existing, "id", 1L);
        ProductionCompany updated = new ProductionCompany();
        updated.setName("New Name");

        when(pCompanyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pCompanyRepository.save(any())).thenReturn(existing);

        ProductionCompany result = productionService.update(updated, 1L);

        assertEquals("New Name", result.getName());
        verify(pCompanyRepository).findById(1L);
        verify(pCompanyRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        ProductionCompany updated = new ProductionCompany();
        when(pCompanyRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productionService.update(updated, 123L));
    }

    @Test
    void testDelete() {
        productionService.delete(1L);
        verify(pCompanyRepository).deleteById(1L);
    }

    @Test
    void testFindByName() {
        ProductionCompany production = new ProductionCompany();
        production.setName("Studio A");

        when(pCompanyRepository.findByName("Studio A")).thenReturn(Optional.of(production));

        Optional<ProductionCompany> result = productionService.findByName("Studio A");

        assertTrue(result.isPresent());
        assertEquals("Studio A", result.get().getName());
        verify(pCompanyRepository).findByName("Studio A");
    }

}