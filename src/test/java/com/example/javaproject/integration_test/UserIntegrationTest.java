package com.example.javaproject.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.javaproject.entity.User;
import com.example.javaproject.repository.UserRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = JavaProjectApplication.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("TestUser1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("TestUser2");
        userRepository.save(user2);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("TestUser1"))
                .andExpect(jsonPath("$[1].username").value("TestUser2"));
    }

    @Test
    void testAddUser() throws Exception {
        User user1 = new User();
        user1.setUsername("TestUser1");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("TestUser1"));
    }

    @Test
    void testDeleteUser() throws Exception {
        User user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/" + user1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("TestUser2");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update/" + user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("TestUser2"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/get/" + user1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("TestUser1"));
    }
}