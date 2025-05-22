package com.example.javaproject.service;

import com.example.javaproject.entity.Student;
import com.example.javaproject.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> getByID(Long id) {
        return studentRepository.findById(id);
    }

    public Student create(Student newStudent) {
        return studentRepository.save(newStudent);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}