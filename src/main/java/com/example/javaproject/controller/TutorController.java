package com.example.javaproject.controller;

import com.example.javaproject.entity.Tutor;
import com.example.javaproject.service.TutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Tutor Controller", description="Tutors management")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @Operation(summary="Get all tutors", description="Receives all tutors")
    @GetMapping("/tutors/get/all")
    public List<Tutor> getAll() {
        return tutorService.getAll();
    }

    @Operation(summary="Get tutor by id", description="Receives tutor with provided id")
    @GetMapping("/tutors/get/{id}")
    public Optional<Tutor> getByID(
            @Parameter(description="Tutor id") @PathVariable Long id
    ) {
        return tutorService.getByID(id);
    }

    @Operation(summary="Creates new tutor", description="Creates new tutor")
    @PostMapping("/tutors/create")
    public Tutor create(@RequestBody Tutor newTutor) {
        return tutorService.create(newTutor);
    }

    @Operation(summary="Deletes tutor", description="Deletes the tutor with the given id")
    @DeleteMapping("/tutors/delete/{id}")
    public void delete(
            @Parameter(description = "Tutor id") @PathVariable Long id
    ) {
        tutorService.delete(id);
    }
}