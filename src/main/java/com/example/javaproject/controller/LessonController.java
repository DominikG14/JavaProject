package com.example.javaproject.controller;

import com.example.javaproject.entity.Lesson;
import com.example.javaproject.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Lesson Controller", description="Lessons management")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Operation(summary="Get all lessons", description="Receives all lessons posted by tutors")
    @GetMapping("/lessons/get/all")
    public List<Lesson> getAll() {
        return lessonService.getAll();
    }

    @Operation(summary="Get lesson by id", description="Receives lesson with provided id")
    @GetMapping("/lessons/get/{id}")
    public Optional<Lesson> getByID(
            @Parameter(description="Lesson id") @PathVariable Long id
    ) {
        return lessonService.getByID(id);
    }

    @Operation(summary="Creates new lesson", description="Creates new lesson with choosen offer by a student")
    @PostMapping("/lessons/create")
    public Lesson create(@RequestBody Lesson newLesson) {
        return lessonService.create(newLesson);
    }

    @Operation(summary="Updates existing lesson", description="Updates a lesson with the given id")
    @PutMapping("/lessons/update/{id}")
    public Optional<Lesson> update(
            @Parameter(description = "Lesson id") @PathVariable Long id,
            @RequestBody Lesson updatedLesson
    ) {
        return lessonService.update(id, updatedLesson);
    }

    @Operation(summary="Deletes lesson by id", description="Deletes the lesson with the given id")
    @DeleteMapping("/lessons/delete/{id}")
    public void delete(
            @Parameter(description = "Lesson id") @PathVariable Long id
    ) {
        lessonService.delete(id);
    }
}