package com.example.javaproject.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.javaproject.entity.*;
import com.example.javaproject.repository.UserRepository;
import com.example.javaproject.repository.TutorRepository;
import com.example.javaproject.repository.OfferRepository;
import com.example.javaproject.JavaProjectApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = JavaProjectApplication.class)
public class OfferIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private OfferRepository offerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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

    private Tutor tutor1;
    private Tutor tutor2;

    @BeforeEach
    void setUp() {
        offerRepository.deleteAll();
        tutorRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);
        tutor1 = new Tutor();
        tutor1.setUser(user1);
        tutor1 = tutorRepository.save(tutor1);

        User user2 = new User();
        user2.setUsername("TestUser2");
        user2 = userRepository.save(user2);
        tutor2 = new Tutor();
        tutor2.setUser(user2);
        tutor2 = tutorRepository.save(tutor2);
    }

    @Test
    void testGetOffers() throws Exception {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);
        offerRepository.save(offer1);

        Offer offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);
        offerRepository.save(offer2);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testAddOffer() throws Exception {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.SREDNIA);
        offer1.setLevel_type(LevelType.PODSTAWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        mockMvc.perform(MockMvcRequestBuilders.post("/offers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteOffer() throws Exception {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.SREDNIA);
        offer1.setLevel_type(LevelType.PODSTAWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);
        offerRepository.save(offer1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/offers/delete/" + offer1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/offers/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateOffer() throws Exception {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);
        offerRepository.save(offer1);

        Offer offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);

        mockMvc.perform(MockMvcRequestBuilders.put("/offers/update/" + offer1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("TestName2"));
    }

    @Test
    void testGetOfferById() throws Exception {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);
        offerRepository.save(offer1);

        mockMvc.perform(MockMvcRequestBuilders.get("/offers/get/" + offer1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}