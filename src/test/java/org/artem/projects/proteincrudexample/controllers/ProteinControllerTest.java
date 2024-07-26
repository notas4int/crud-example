package org.artem.projects.proteincrudexample.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.artem.projects.proteincrudexample.daos.ProteinDaoImpl;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
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
    ProteinDaoImpl proteinDao;

    @Autowired
    ProteinController proteinController;

    @Autowired
    private SessionFactory sessionFactory;

    ObjectMapper objectMapper;

    @BeforeAll
    void init() {

    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void cleanDatabase() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Protein").executeUpdate();
            session.getTransaction().commit();
        }
    }


    @Test
    void shouldReturnProtein_AfterGettingRequest() throws Exception {
        Protein protein = new Protein(1, "Protein", "Brand", 100);
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(protein);
            session.getTransaction().commit();
        }

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
        Protein protein = new Protein(3, "Protein", "Brand", 100);
        String json = objectMapper.writeValueAsString(protein);

        var request = post("/api/protein/createProtein")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                content().json(json)
        );

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            assertEquals(protein, session.get(Protein.class, 3L));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateProteinAndReturnUpdatedObj_AfterUpdatingRequest() throws Exception {
        Protein protein = new Protein(4, "Protein", "Brand", 100);
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(protein);
            session.detach(protein);
            session.getTransaction().commit();
        }

        protein.setBrand("First russian protein");
        String json = objectMapper.writeValueAsString(protein);

        var request = patch("/api/protein/updateProtein")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                content().json(json)
        );

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            assertEquals(protein, session.get(Protein.class, 4L));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldReturnExceptionResponse_IfProteinNotFound_AfterUpdatingRequest() throws Exception {
        Protein protein = new Protein(5, "Protein", "Brand", 100);
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
        Protein protein = Protein.builder()
                .name("Protein")
                .brand("Brand")
                .cost(100).build();

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            protein = session.merge(protein);
            session.getTransaction().commit();
        }

        System.out.println(protein);

        var request = delete("/api/protein/deleteProtein/{id}", protein.getId());

        mockMvc.perform(request).andExpectAll(
                status().isOk()
        );

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            assertEquals(Optional.empty(), Optional.ofNullable(session.get(Protein.class, 1L)));
            session.getTransaction().commit();
        }
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