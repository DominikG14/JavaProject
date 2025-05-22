package com.example.javaproject.controller;

import com.example.javaproject.entity.User;
import com.example.javaproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="User Controller", description="Users management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary="Get all users", description="Receives all users")
    @GetMapping("/users/get/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @Operation(summary="Get user by id", description="Receives user with provided id")
    @GetMapping("/users/get/{id}")
    public Optional<User> getByID(
            @Parameter(description="User id") @PathVariable Long id
    ) {
        return userService.getByID(id);
    }

    @Operation(summary="Creates new user", description="Creates new user")
    @PostMapping("/users/create")
    public User create(@RequestBody User newUser) {
        return userService.create(newUser);
    }

    @Operation(summary="Updates existing user", description="Updates a user with the given id")
    @PutMapping("/users/update/{id}")
    public Optional<User> update(
            @Parameter(description = "User id") @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        return userService.update(id, updatedUser);
    }

    @Operation(summary="Deletes user", description="Deletes the user with the given id")
    @DeleteMapping("/users/delete/{id}")
    public void delete(
            @Parameter(description = "User id") @PathVariable Long id
    ) {
        userService.delete(id);
    }
}