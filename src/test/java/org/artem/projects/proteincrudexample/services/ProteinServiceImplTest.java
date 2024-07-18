package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class ProteinServiceImplTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private ProteinServiceImpl proteinService;

    private Protein protein;

    @BeforeEach
    void setUp() {
        protein = new Protein(1, "Protein", "Brand", 100);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    void shouldReturnProteinObject_AfterFindById() {
        long requiredId = 1;
        when(session.get(Protein.class, requiredId)).thenReturn(protein);

        assertEquals(protein, proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterFindingByWrongId() {
        long requiredId = 2;
        when(session.get(Protein.class, requiredId)).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.findProteinById(requiredId));
    }

    @Test
    void shouldReturnProtein_AfterCreatingProtein() {
        doNothing().when(session).persist(any(Protein.class));
        doNothing().when(transaction).commit();

//        assertEquals(protein, proteinService.saveProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterUpdatingProtein() {
        when(session.get(Protein.class, protein.getId())).thenReturn(protein);
        doNothing().when(session).merge(any(Protein.class));
        doNothing().when(transaction).commit();

        assertEquals(protein, proteinService.updateProtein(protein));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterUpdatingProtein() {
        Protein protein = new Protein(2, "Protein", "Brand", 100);
        when(session.get(Protein.class, protein.getId())).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.updateProtein(protein));
    }

    @Test
    void shouldReturnRightChangedRows_AfterDeletingProtein() {
        long requiredId = 1;
        when(session.get(Protein.class, requiredId)).thenReturn(protein);
        doNothing().when(session).remove(any(Protein.class));
        doNothing().when(transaction).commit();

        assertTrue(proteinService.deleteProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterDeletingProtein() {
        long requiredId = 2;
        when(session.get(Protein.class, requiredId)).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinService.deleteProteinById(requiredId));
    }
}