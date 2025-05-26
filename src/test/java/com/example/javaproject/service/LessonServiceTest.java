
package com.example.javaproject.service;

import com.example.javaproject.entity.Lesson;
import com.example.javaproject.entity.SchoolType;
import com.example.javaproject.entity.LevelType;
import com.example.javaproject.entity.User;
import com.example.javaproject.entity.Tutor;
import com.example.javaproject.entity.Student;
import com.example.javaproject.entity.Offer;
import com.example.javaproject.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LessonServiceTest {

    private LessonRepository lessonRepository;
    private LessonService lessonService;

    private Student student1;
    private Student student2;

    private Tutor tutor1;
    private Tutor tutor2;

    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    public void setUp() {
        lessonRepository = mock(LessonRepository.class);
        lessonService = new LessonService(lessonRepository);

        User user1 = new User();
        user1.setUsername("TestTutor1");
        tutor1 = new Tutor();
        tutor1.setUser(user1);

        User user2 = new User();
        user2.setUsername("TestTutor2");
        tutor2 = new Tutor();
        tutor2.setUser(user2);

        user1 = new User();
        user1.setUsername("TestStudent1");
        student1 = new Student();
        student1.setUser(user1);

        user2 = new User();
        user2.setUsername("TestStudent2");
        student2 = new Student();
        student2.setUser(user2);

        offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);
    }

    @Test
    @DisplayName("Should return all lessons")
    public void testGetAllLessons() {
        Lesson lesson1 = new Lesson();
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);

        Lesson lesson2 = new Lesson();
        lesson2.setOffer(offer2);
        lesson2.setStudent(student2);

        when(lessonRepository.findAll()).thenReturn(Arrays.asList(lesson1, lesson2));
        List<Lesson> lessons = lessonService.getAll();

        assertEquals(2, lessons.size());
        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return lesson by ID")
    public void testGetByID() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson1));

        Optional<Lesson> result = lessonService.getByID(1L);

        assertTrue(result.isPresent());
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new lesson")
    public void testCreateLesson() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);

        when(lessonRepository.save(lesson1)).thenReturn(lesson1);

        Lesson createdLesson = lessonService.create(lesson1);

        assertNotNull(createdLesson);
        verify(lessonRepository, times(1)).save(lesson1);
    }

    @Test
    @DisplayName("Should update an existing lesson")
    public void testUpdateLesson() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setOffer(offer1);
        lesson1.setStudent(student1);

        Lesson lesson2 = new Lesson();
        lesson2.setOffer(offer2);
        lesson2.setStudent(student2);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson1));
        when(lessonRepository.save(lesson1)).thenReturn(lesson1);

        Optional<Lesson> result = lessonService.update(1L, lesson2);

        assertTrue(result.isPresent());
        assertEquals(student2, result.get().getStudent());
        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(lesson1);
    }

    @Test
    @DisplayName("Should delete an existing lesson")
    public void testDeleteLesson() {
        lessonService.delete(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }
}
