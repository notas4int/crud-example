package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinCreatingException;
import org.artem.projects.proteincrudexample.exceptions.ProteinDeletingException;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.artem.projects.proteincrudexample.exceptions.ProteinUpdatingException;
import org.artem.projects.proteincrudexample.repositories.ProteinRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProteinServiceImplTest {
    @Mock
    private ProteinRepositoryImpl proteinRepository;

    @InjectMocks
    private ProteinServiceImpl proteinService;

    private Protein protein;

    @BeforeEach
    void setUp() {
        protein = new Protein(1, "Protein", "Brand", 100);
    }

    @Test
    void shouldReturnProteinObject_AfterFindById() {
        Protein exceptedProtein = new Protein(1, "Protein", "Brand", 100);
        int requiredId = 1;

        when(proteinRepository.getProtein(requiredId)).thenReturn(exceptedProtein);

        assertEquals(exceptedProtein, proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterFindByWrongId() {
        int requiredId = 2;

        when(proteinRepository.getProtein(requiredId)).thenThrow(ProteinNotFoundException.class);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldReturnRightChangedRows_AfterCreatingProtein() {
        when(proteinRepository.createProtein(protein)).thenReturn(1);

        assertEquals(1, proteinService.saveProtein(protein));
    }

    @Test
    void shouldThrowProteinCreatingException_AfterCreatingProtein() {
        Protein protein = new Protein(2, "Protein", "Brand", 100);
        when(proteinRepository.createProtein(protein)).thenThrow(ProteinCreatingException.class);

        assertThrows(ProteinCreatingException.class, () -> proteinService.saveProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterUpdatingProtein() {
        when(proteinRepository.updateProtein(protein)).thenReturn(1);

        assertEquals(1, proteinService.updateProtein(protein));
    }

    @Test
    void shouldThrowProteinUpdatingException_AfterUpdatingProtein() {
        Protein protein = new Protein(2, "Protein", "Brand", 100);
        when(proteinRepository.updateProtein(protein)).thenThrow(ProteinUpdatingException.class);

        assertThrows(ProteinUpdatingException.class, () -> proteinService.updateProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterDeletingProtein() {
        int requiredId = 1;

        when(proteinRepository.deleteProtein(requiredId)).thenReturn(1);

        assertEquals(1, proteinService.deleteProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinDeletingException_AfterDeletingProtein() {
        int requiredId = 2;

        when(proteinRepository.deleteProtein(requiredId)).thenThrow(ProteinDeletingException.class);

        assertThrows(ProteinDeletingException.class, () -> proteinService.deleteProteinById(requiredId));
    }
}