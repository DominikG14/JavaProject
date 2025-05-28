package com.example.javaproject.integration_test;

import com.example.javaproject.JavaProjectApplication;
import com.example.javaproject.config.TestSecurityConfig;
import com.example.javaproject.entity.Admin;
import com.example.javaproject.entity.User;
import com.example.javaproject.repository.AdminRepository;
import com.example.javaproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(classes = JavaProjectApplication.class)
@Import(TestSecurityConfig.class)
public class AdminIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AdminRepository adminRepository;
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
        adminRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new User();
        user1.setUsername("TestUser1");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("TestUser2");
        user2 = userRepository.save(user2);
    }

    @Test
    void testGetAdmins() throws Exception {
        Admin admin1 = new Admin();
        admin1.setUser(user1);
        admin1 = adminRepository.save(admin1);

        Admin admin2 = new Admin();
        admin2.setUser(user2);
        admin2 = adminRepository.save(admin2);

        mockMvc.perform(MockMvcRequestBuilders.get("/admins/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testAddAdmin() throws Exception {
        Admin admin1 = new Admin();
        admin1.setUser(user1);

        mockMvc.perform(MockMvcRequestBuilders.post("/admins/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteAdmin() throws Exception {
        Admin admin1 = new Admin();
        admin1.setUser(user1);
        admin1 = adminRepository.save(admin1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admins/delete/" + admin1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/admins/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAdminById() throws Exception {
        Admin admin1 = new Admin();
        admin1.setUser(user1);
        admin1 = adminRepository.save(admin1);

        mockMvc.perform(MockMvcRequestBuilders.get("/admins/get/" + admin1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}