package com.example.javaproject.service;

import com.example.javaproject.entity.Admin;
import com.example.javaproject.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getByID(Long id) {
        return adminRepository.findById(id);
    }

    public Admin create(Admin newAdmin) {
        return adminRepository.save(newAdmin);
    }

    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}