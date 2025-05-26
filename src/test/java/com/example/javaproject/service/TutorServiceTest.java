
package com.example.javaproject.service;

import com.example.javaproject.entity.User;
import com.example.javaproject.entity.Tutor;
import com.example.javaproject.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TutorServiceTest {

    private TutorRepository tutorRepository;
    private TutorService tutorService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        tutorRepository = mock(TutorRepository.class);
        tutorService = new TutorService(tutorRepository);

        user1 = new User();
        user1.setUsername("TestUser1");

        user2 = new User();
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Should return all tutors")
    public void testGetAllTutors() {
        Tutor tutor1 = new Tutor();
        tutor1.setUser(user1);

        Tutor tutor2 = new Tutor();
        tutor2.setUser(user2);

        when(tutorRepository.findAll()).thenReturn(Arrays.asList(tutor1, tutor2));
        List<Tutor> tutors = tutorService.getAll();

        assertEquals(2, tutors.size());
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return tutor by ID")
    public void testGetByID() {
        Tutor tutor = new Tutor();
        tutor.setId(1L);
        tutor.setUser(user1);

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        Optional<Tutor> result = tutorService.getByID(1L);

        assertTrue(result.isPresent());
        verify(tutorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new tutor")
    public void testCreateTutor() {
        Tutor newTutor = new Tutor();
        newTutor.setUser(user1);

        when(tutorRepository.save(newTutor)).thenReturn(newTutor);

        Tutor createdTutor = tutorService.create(newTutor);

        assertNotNull(createdTutor);
        verify(tutorRepository, times(1)).save(newTutor);
    }

    @Test
    @DisplayName("Should delete an existing tutor")
    public void testDeleteTutor() {
        tutorService.delete(1L);
        verify(tutorRepository, times(1)).deleteById(1L);
    }
}
