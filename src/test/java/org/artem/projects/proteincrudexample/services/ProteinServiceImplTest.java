package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.artem.projects.proteincrudexample.repositories.ProteinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProteinServiceImplTest {
    @Mock
    private ProteinRepository proteinRepository;

    @InjectMocks
    private ProteinServiceImpl proteinService;

    private Protein protein;

    @BeforeEach
    void setUp() {
        protein = new Protein(1, "Protein", "Brand", 100);
    }

    @Test
    void shouldReturnProteinObject_AfterFindById() {
        Optional<Protein> exceptedOptionalProtein = Optional.of(new Protein(1, "Protein", "Brand", 100));
        long requiredId = 1;

        when(proteinRepository.findById(1L)).thenReturn(exceptedOptionalProtein);

        assertEquals(exceptedOptionalProtein.get(), proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterFindingByWrongId() {
        long requiredId = 2;

        when(proteinRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProteinNotFoundException.class, () -> proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldReturnProtein_AfterCreatingProtein() {
        Protein exceptedProtein = new Protein(1, "Protein", "Brand", 100);

        when(proteinRepository.save(protein)).thenReturn(exceptedProtein);

        assertEquals(exceptedProtein, proteinService.saveProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterUpdatingProtein() {
        when(proteinRepository.existsById(1L)).thenReturn(true);
        when(proteinRepository.save(protein)).thenReturn(protein);

        assertEquals(protein, proteinService.updateProtein(protein));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterUpdatingProtein() {
        Protein protein = new Protein(2, "Protein", "Brand", 100);
        when(proteinRepository.existsById(2L)).thenReturn(false);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.updateProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterDeletingProtein() {
        long requiredId = 1;
        when(proteinRepository.existsById(1L)).thenReturn(true);

        assertTrue(proteinService.deleteProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterDeletingProtein() {
        int requiredId = 2;

        when(proteinRepository.existsById(2L)).thenReturn(false);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.deleteProteinById(requiredId));
    }
}