package com.example.javaproject.controller;

import com.example.javaproject.entity.Student;
import com.example.javaproject.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Student Controller", description="Students management")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary="Get all students", description="Receives all students")
    @GetMapping("/students/get/all")
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @Operation(summary="Get student by id", description="Receives student with provided id")
    @GetMapping("/students/get/{id}")
    public Optional<Student> getByID(
            @Parameter(description="Student id") @PathVariable Long id
    ) {
        return studentService.getByID(id);
    }

    @Operation(summary="Creates new student", description="Creates new student")
    @PostMapping("/students/create")
    public Student create(@RequestBody Student newStudent) {
        return studentService.create(newStudent);
    }

    @Operation(summary="Deletes student", description="Deletes the student with the given id")
    @DeleteMapping("/students/delete/{id}")
    public void delete(
            @Parameter(description = "Student id") @PathVariable Long id
    ) {
        studentService.delete(id);
    }
}