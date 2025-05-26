package com.example.javaproject.integration_test;

import com.example.javaproject.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.javaproject.entity.*;
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
public class LessonIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private OfferRepository offerRepository;
    @Autowired private LessonRepository lessonRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private StudentRepository studentRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Student student1;
    private Student student2;

    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        offerRepository.deleteAll();
        tutorRepository.deleteAll();
        studentRepository.deleteAll();
        userRepository.deleteAll();

        User user_student_1 = new User();
        user_student_1.setUsername("TestStudent1");
        user_student_1 = userRepository.save(user_student_1);
        student1 = new Student();
        student1.setUser(user_student_1);
        student1 = studentRepository.save(student1);

        User user_student_2 = new User();
        user_student_2.setUsername("TestStudent2");
        user_student_2 = userRepository.save(user_student_2);
        student2 = new Student();
        student2.setUser(user_student_2);
        student2 = studentRepository.save(student2);

        User user_tutor_1 = new User();
        user_tutor_1.setUsername("TestTutor1");
        user_tutor_1 = userRepository.save(user_tutor_1);
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user_tutor_1);
        tutor1 = tutorRepository.save(tutor1);

        User user_tutor_2 = new User();
        user_tutor_2.setUsername("TestTutor2");
        user_tutor_2 = userRepository.save(user_tutor_2);
        Tutor tutor2 = new Tutor();
        tutor2.setUser(user_tutor_2);
        tutor2 = tutorRepository.save(tutor2);

        offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);
        offer1 = offerRepository.save(offer1);

        offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);
        offer2 = offerRepository.save(offer2);
    }

    @Test
    void testGetAllLessons() throws Exception {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);
        lesson1 = lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setOffer(offer2);
        lesson2.setStudent(student2);
        lesson2 = lessonRepository.save(lesson2);

        mockMvc.perform(MockMvcRequestBuilders.get("/lessons/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testAddLesson() throws Exception {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);

        mockMvc.perform(MockMvcRequestBuilders.post("/lessons/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteLesson() throws Exception {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);
        lesson1 = lessonRepository.save(lesson1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/lessons/delete/" + lesson1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/lessons/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateLesson() throws Exception {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);
        lesson1 = lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setOffer(offer2);
        lesson2.setStudent(student2);

        mockMvc.perform(MockMvcRequestBuilders.put("/lessons/update/" + lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.student.user.username").value("TestStudent2"));
    }

    @Test
    void testGetLessonById() throws Exception {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);
        lesson1 = lessonRepository.save(lesson1);

        mockMvc.perform(MockMvcRequestBuilders.get("/lessons/get/" + lesson1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}