package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dal.repositories.EquipementRepository;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.Equipement;
import be.bdus.rush_api.dl.entities.RentingCompany;
import be.bdus.rush_api.dl.enums.EquipementCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EquipementServiceImplTest {

    private EquipementRepository equipementRepository;
    private EquipementServiceImpl equipementService;
    private RentingServiceImpl locationService;
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        equipementRepository = mock(EquipementRepository.class);
        userRepository = mock(UserRepository.class);
        locationService = mock(RentingServiceImpl.class);

        equipementService = new EquipementServiceImpl(equipementRepository, userRepository, locationService);
    }

    @Test
    void testFindAll() {
        Page<Equipement> mockPage = new PageImpl<>(Collections.emptyList());
        when(equipementRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Equipement> result = equipementService.findAll(PageRequest.of(0, 10));
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testFindById_found() {
        Equipement expectedEquipement = new Equipement();

        when(equipementRepository.findById(1L)).thenReturn(Optional.of(expectedEquipement));

        Equipement result = equipementService.findById(1L);
        assertEquals(expectedEquipement, result);
    }

    @Test
    void testFindById_notFound() {
        when(equipementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> equipementService.findById(1L));
    }

    @Test
    void testFindBySerialNumber_Found() {
        Equipement equipement = new Equipement();
        when(equipementRepository.findBySerialNumber("123ABC")).thenReturn(Optional.of(equipement));

        Equipement result = equipementService.findBySerialNumber("123ABC");
        assertNotNull(result);
    }

    @Test
    void testFindBySerialNumber_NotFound() {
        when(equipementRepository.findBySerialNumber("XYZ")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> equipementService.findBySerialNumber("XYZ"));
    }

    @Test
    void testFindByOwnerId() {
        Page<Equipement> mockPage = new PageImpl<>(Collections.emptyList());
        when(equipementRepository.findByOwnerId(eq(5L), any())).thenReturn(mockPage);

        Page<Equipement> result = equipementService.findByOwnerId(5L, PageRequest.of(0, 10));
        assertNotNull(result);
    }

    @Test
    void testSave_WithNewOwner() {
        Equipement equipement = new Equipement();
        RentingCompany newOwner = new RentingCompany();
        newOwner.setName("NewOwner");
        equipement.setOwner(newOwner);

        when(locationService.findByName("NewOwner")).thenReturn(Optional.empty());
        when(locationService.save(any())).thenReturn(newOwner);
        when(equipementRepository.save(equipement)).thenReturn(equipement);

        Equipement result = equipementService.save(equipement);
        assertNotNull(result);
    }

    @Test
    void testSave_WithExistingOwner() {
        Equipement equipement = new Equipement();
        RentingCompany existingOwner = new RentingCompany();
        existingOwner.setName("Existing");

        equipement.setOwner(existingOwner);

        when(locationService.findByName("Existing")).thenReturn(Optional.of(existingOwner));
        when(equipementRepository.save(any(Equipement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipement result = equipementService.save(equipement);

        assertNotNull(result);
        assertEquals("Existing", result.getOwner().getName());
        verify(locationService).findByName("Existing");
        verify(equipementRepository).save(equipement);
    }

    @Test
    void testDelete() {
        equipementService.delete(10L);
        verify(equipementRepository, times(1)).deleteById(10L);
    }

    @Test
    void testUpdate_Found() {
        Equipement toUpdate = new Equipement();
        toUpdate.setName("Updated");

        Equipement existing = new Equipement();
        when(equipementRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(equipementRepository.save(any())).thenReturn(existing);

        Equipement result = equipementService.update(toUpdate, 1L);
        assertNotNull(result);
        verify(equipementRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        Equipement dummy = new Equipement();
        when(equipementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> equipementService.update(dummy, 999L));
    }

    @Test
    void testFindEquipementsToRevise() {
        // Given
        Equipement oldEquipement = new Equipement();
        oldEquipement.setDateLastRevision(LocalDate.now().minusMonths(7));

        Equipement recentEquipement = new Equipement();
        recentEquipement.setDateLastRevision(LocalDate.now().minusMonths(3));

        Equipement noRevisionDate = new Equipement();
        noRevisionDate.setDateLastRevision(null);

        when(equipementRepository.findAll()).thenReturn(List.of(oldEquipement, recentEquipement, noRevisionDate));

        // When
        List<Equipement> result = equipementService.findEquipementsToRevise();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(oldEquipement));
    }

    @Test
    void testPlanNextRevisionForEquipements() {
        // Given
        Equipement toRevise = new Equipement();
        toRevise.setDateLastRevision(LocalDate.now().minusMonths(7));
        toRevise.setCondition(EquipementCondition.TO_BE_REVISED);

        when(equipementRepository.findAll()).thenReturn(List.of(toRevise));

        // When
        equipementService.planNextRevisionForEquipements();

        // Then
        assertEquals(LocalDate.now().plusWeeks(2), toRevise.getPlannedRevisionDate());
        assertEquals(EquipementCondition.TO_BE_REVISED, toRevise.getCondition());
        verify(equipementRepository).saveAll(List.of(toRevise));
    }

}