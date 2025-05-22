package com.example.javaproject.service;

import com.example.javaproject.entity.Lesson;
import com.example.javaproject.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getByID(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson create(Lesson newLesson) {
        return lessonRepository.save(newLesson);
    }

    public Optional<Lesson> update(Long id, Lesson updatedLesson) {
        return lessonRepository.findById(id).map(project -> {
            project.setStudent(updatedLesson.getStudent());
            project.setOffer(updatedLesson.getOffer());
            return lessonRepository.save(project);
        });
    }

    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }
}
