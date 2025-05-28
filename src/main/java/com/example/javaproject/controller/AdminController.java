package com.example.javaproject.controller;

import com.example.javaproject.entity.Admin;
import com.example.javaproject.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Admin Controller", description="Admins management")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary="Get all admins", description="Receives all admins")
    @GetMapping("/admins/get/all")
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    @Operation(summary="Get admin by id", description="Receives admin with provided id")
    @GetMapping("/admins/get/{id}")
    public Optional<Admin> getByID(
            @Parameter(description="Admin id") @PathVariable Long id
    ) {
        return adminService.getByID(id);
    }

    @Operation(summary="Creates new admin", description="Creates new admin")
    @PostMapping("/admins/create")
    public Admin create(@RequestBody Admin newAdmin) {
        return adminService.create(newAdmin);
    }

    @Operation(summary="Deletes admin", description="Deletes the admin with the given id")
    @DeleteMapping("/admins/delete/{id}")
    public void delete(
            @Parameter(description = "Admin id") @PathVariable Long id
    ) {
        adminService.delete(id);
    }
}