
package com.example.javaproject.service;

import com.example.javaproject.entity.Admin;
import com.example.javaproject.entity.User;
import com.example.javaproject.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private AdminRepository adminRepository;
    private AdminService adminService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        adminRepository = mock(AdminRepository.class);
        adminService = new AdminService(adminRepository);

        user1 = new User();
        user1.setUsername("TestUser1");

        user2 = new User();
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Should return all admins")
    public void testGetAllAdmins() {
        Admin admin1 = new Admin();
        admin1.setUser(user1);

        Admin admin2 = new Admin();
        admin2.setUser(user2);

        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin1, admin2));
        List<Admin> admins = adminService.getAll();

        assertEquals(2, admins.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return admin by ID")
    public void testGetByID() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUser(user1);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<Admin> result = adminService.getByID(1L);

        assertTrue(result.isPresent());
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new admin")
    public void testCreateAdmin() {
        Admin newAdmin = new Admin();
        newAdmin.setUser(user1);

        when(adminRepository.save(newAdmin)).thenReturn(newAdmin);

        Admin createdAdmin = adminService.create(newAdmin);

        assertNotNull(createdAdmin);
        verify(adminRepository, times(1)).save(newAdmin);
    }

    @Test
    @DisplayName("Should delete an existing admin")
    public void testDeleteAdmin() {
        adminService.delete(1L);
        verify(adminRepository, times(1)).deleteById(1L);
    }
}
