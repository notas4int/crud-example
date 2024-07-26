package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.daos.ProteinDaoImpl;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProteinDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private ProteinDaoImpl proteinDao;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    private Protein protein;

    @BeforeEach
    void setUp() {
        protein = new Protein(1, "Protein", "Brand", 100);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doNothing().when(session).close();
    }

    @Test
    void shouldReturnProteinObject_AfterFindById() {
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        Protein returnedProtein = new Protein(1, "Protein", "Brand", 100);
        long requiredId = 1;
        when(session.get(Protein.class, requiredId)).thenReturn(protein);

        assertEquals(returnedProtein, proteinDao.findProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterFindingByWrongId() {
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        long requiredId = 2;
        when(session.get(Protein.class, requiredId)).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinDao.findProteinById(requiredId));
    }

    @Test
    void shouldReturnProtein_AfterCreatingProtein() {
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        Protein savedProtein = new Protein(1, "Protein", "Brand", 100);
        doNothing().when(session).persist(protein);

        assertEquals(savedProtein, proteinDao.saveProtein(protein));
    }

    @Test
    void shouldReturnProteinObject_AfterUpdatingProtein() {
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        Protein returnedProtein = new Protein(1, "Protein", "Brand", 100);
        when(session.get(Protein.class, protein.getId())).thenReturn(protein);
        when(session.merge(protein)).thenReturn(protein);

        assertEquals(returnedProtein, proteinDao.updateProtein(protein));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterUpdatingProtein() {
        Protein protein = new Protein(2, "Protein", "Brand", 100);
        when(session.get(Protein.class, protein.getId())).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinDao.updateProtein(protein));
    }

    @Test
    void shouldReturnTrue_AfterDeletingProtein() {
        when(session.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).commit();

        long requiredId = 1;
        when(session.get(Protein.class, requiredId)).thenReturn(protein);
        doNothing().when(session).remove(protein);

        assertTrue(proteinDao.deleteProteinById(requiredId));
    }

    @Test
    void shouldThrowProteinNotFoundException_AfterDeletingProtein() {
        long requiredId = 2;
        when(session.get(Protein.class, requiredId)).thenReturn(null);

        assertThrows(ProteinNotFoundException.class, () -> proteinDao.deleteProteinById(requiredId));
    }
}