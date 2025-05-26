
package com.example.javaproject.service;

import com.example.javaproject.entity.User;
import com.example.javaproject.entity.Student;
import com.example.javaproject.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private StudentRepository studentRepository;
    private StudentService studentService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);

        user1 = new User();
        user1.setUsername("TestUser1");

        user2 = new User();
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Should return all students")
    public void testGetAllStudents() {
        Student student1 = new Student();
        student1.setUser(user1);

        Student student2 = new Student();
        student2.setUser(user2);

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        List<Student> students = studentService.getAll();

        assertEquals(2, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return student by ID")
    public void testGetByID() {
        Student student = new Student();
        student.setId(1L);
        student.setUser(user1);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.getByID(1L);

        assertTrue(result.isPresent());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new student")
    public void testCreateStudent() {
        Student newStudent = new Student();
        newStudent.setUser(user1);

        when(studentRepository.save(newStudent)).thenReturn(newStudent);

        Student createdStudent = studentService.create(newStudent);

        assertNotNull(createdStudent);
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    @DisplayName("Should delete an existing student")
    public void testDeleteStudent() {
        studentService.delete(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
