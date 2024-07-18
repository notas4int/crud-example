package org.artem.projects.proteincrudexample.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.artem.projects.proteincrudexample.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProteinControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProteinController proteinController;

    @PersistenceContext
    private EntityManager entityManager;

    ObjectMapper objectMapper;

    Session session;

    @BeforeAll
    void init() {
//        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterAll
    void close() {
        session.close();
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void cleanDatabase() {
        entityManager.createQuery("delete from Protein").executeUpdate();
    }


    @Test
    void shouldReturnProtein_AfterGettingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        session.beginTransaction();
        session.persist(protein);
        session.getTransaction().commit();

        String json = objectMapper.writeValueAsString(protein);
        var request = get("/api/protein/getProtein/{id}", 1L);

        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                content().json(json)
        );
    }

    @Test
    void shouldReturnExceptionResponse_IfProteinNotFound_AfterGettingRequest() throws Exception {
        var request = get("/api/protein/getProtein/{id}", 2L);

        mockMvc.perform(request).andExpectAll(
                result -> assertInstanceOf(ProteinNotFoundException.class, result.getResolvedException()),
                status().isNotFound(),
                jsonPath("$.requestURI").value("/api/protein/getProtein/2"),
                jsonPath("$.message").value("Protein not found"),
                jsonPath("$.currentTime").exists()
        );
    }

    @Test
    void shouldCreateProteinAndReturnSavedObj_AfterCreatingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        String json = objectMapper.writeValueAsString(protein);

        var request = post("/api/protein/createProtein")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                content().json(json)
        );

        session.beginTransaction();
        assertEquals(protein,session.get(Protein.class, 1L));
        session.getTransaction().commit();
    }

    @Test
    void shouldUpdateProteinAndReturnUpdatedObj_AfterUpdatingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        session.beginTransaction();
        session.persist(protein);
        session.getTransaction().commit();

        session.detach(protein);
        protein.setBrand("First russian protein");
        String json = objectMapper.writeValueAsString(protein);

        var request = patch("/api/protein/updateProtein")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                content().json(json)
        );

        session.beginTransaction();
        assertEquals(protein,session.get(Protein.class, 1L));
        session.getTransaction().commit();
    }

    @Test
    void shouldReturnExceptionResponse_IfProteinNotFound_AfterUpdatingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        String json = objectMapper.writeValueAsString(protein);

        var request = patch("/api/protein/updateProtein")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request).andExpectAll(
                result -> assertInstanceOf(ProteinNotFoundException.class, result.getResolvedException()),
                status().isNotFound(),
                jsonPath("$.requestURI").value("/api/protein/updateProtein"),
                jsonPath("$.message").value("Protein not found"),
                jsonPath("$.currentTime").exists()
        );
    }

    @Test
    void shouldDeleteProtein_AfterDeletingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        session.beginTransaction();
        session.persist(protein);
        session.getTransaction().commit();

        var request = delete("/api/protein/deleteProtein/{id}", 1L);

        mockMvc.perform(request).andExpectAll(
                status().isOk()
        );

        session.beginTransaction();
        assertEquals(Optional.empty(), Optional.of(session.get(Protein.class, 1L)));
        session.getTransaction().commit();
    }

    @Test
    void shouldReturnExceptionResponse_IfProteinNotFound_AfterDeletingRequest() throws Exception {
        var request = delete("/api/protein/deleteProtein/{id}", 1L);

        mockMvc.perform(request).andExpectAll(
                result -> assertInstanceOf(ProteinNotFoundException.class, result.getResolvedException()),
                status().isNotFound(),
                jsonPath("$.requestURI").value("/api/protein/deleteProtein/1"),
                jsonPath("$.message").value("Protein not found"),
                jsonPath("$.currentTime").exists()
        );
    }
}