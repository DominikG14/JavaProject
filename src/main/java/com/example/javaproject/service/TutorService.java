package com.example.javaproject.service;

import com.example.javaproject.entity.Tutor;
import com.example.javaproject.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {
    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> getAll() {
        return tutorRepository.findAll();
    }

    public Optional<Tutor> getByID(Long id) {
        return tutorRepository.findById(id);
    }

    public Tutor create(Tutor newTutor) {
        return tutorRepository.save(newTutor);
    }

    public void delete(Long id) {
        tutorRepository.deleteById(id);
    }
}