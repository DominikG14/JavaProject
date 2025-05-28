package com.example.javaproject.integration_test;

import com.example.javaproject.config.TestSecurityConfig;
import com.example.javaproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.javaproject.entity.User;
import com.example.javaproject.entity.Tutor;
import com.example.javaproject.repository.TutorRepository;
import com.example.javaproject.JavaProjectApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = JavaProjectApplication.class)
@Import(TestSecurityConfig.class)
public class TutorIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        tutorRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("TestUser2");
        user2 = userRepository.save(user2);
    }

    @Test
    void testGetTutors() throws Exception {
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user1);
        tutor1 = tutorRepository.save(tutor1);

        Tutor tutor2 = new Tutor();
        tutor2.setUser(user2);
        tutor2 = tutorRepository.save(tutor2);

        mockMvc.perform(MockMvcRequestBuilders.get("/tutors/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testAddTutor() throws Exception {
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user1);

        mockMvc.perform(MockMvcRequestBuilders.post("/tutors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutor1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteTutor() throws Exception {
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user1);
        tutor1 = tutorRepository.save(tutor1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tutors/delete/" + tutor1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/tutors/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetTutorById() throws Exception {
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user1);
        tutor1 = tutorRepository.save(tutor1);

        mockMvc.perform(MockMvcRequestBuilders.get("/tutors/get/" + tutor1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}