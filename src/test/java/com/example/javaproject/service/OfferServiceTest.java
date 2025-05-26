
package com.example.javaproject.service;

import com.example.javaproject.entity.Offer;
import com.example.javaproject.entity.SchoolType;
import com.example.javaproject.entity.LevelType;
import com.example.javaproject.entity.User;
import com.example.javaproject.entity.Tutor;
import com.example.javaproject.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferServiceTest {

    private OfferRepository offerRepository;
    private OfferService offerService;

    private Tutor tutor1;
    private Tutor tutor2;

    @BeforeEach
    public void setUp() {
        offerRepository = mock(OfferRepository.class);
        offerService = new OfferService(offerRepository);

        User user1 = new User();
        user1.setUsername("TestUser1");
        tutor1 = new Tutor();
        tutor1.setUser(user1);

        User user2 = new User();
        user2.setUsername("TestUser2");
        tutor2 = new Tutor();
        tutor2.setUser(user2);
    }

    @Test
    @DisplayName("Should return all offers")
    public void testGetAllOffers() {
        Offer offer1 = new Offer();
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        Offer offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);

        when(offerRepository.findAll()).thenReturn(Arrays.asList(offer1, offer2));
        List<Offer> offers = offerService.getAll();

        assertEquals(2, offers.size());
        verify(offerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return offer by ID")
    public void testGetByID() {
        Offer offer1 = new Offer();
        offer1.setId(1L);
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.SREDNIA);
        offer1.setLevel_type(LevelType.PODSTAWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));

        Optional<Offer> result = offerService.getByID(1L);

        assertTrue(result.isPresent());
        assertEquals("TestName1", result.get().getName());
        verify(offerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new offer")
    public void testCreateOffer() {
        Offer offer1 = new Offer();
        offer1.setId(1L);
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.SREDNIA);
        offer1.setLevel_type(LevelType.PODSTAWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        when(offerRepository.save(offer1)).thenReturn(offer1);

        Offer createdOffer = offerService.create(offer1);

        assertNotNull(createdOffer);
        assertEquals("TestName1", createdOffer.getName());
        verify(offerRepository, times(1)).save(offer1);
    }

    @Test
    @DisplayName("Should update an existing offer")
    public void testUpdateOffer() {
        Offer offer1 = new Offer();
        offer1.setId(1L);
        offer1.setSubject("TestSubject1");
        offer1.setName("TestName1");
        offer1.setDescription("TestDescription1");
        offer1.setSchool_type(SchoolType.PODSTAWOWA);
        offer1.setLessonDateTime(LocalDateTime.now());
        offer1.setTutor(tutor1);

        Offer offer2 = new Offer();
        offer2.setSubject("TestSubject2");
        offer2.setName("TestName2");
        offer2.setDescription("TestDescription2");
        offer2.setSchool_type(SchoolType.SREDNIA);
        offer2.setLevel_type(LevelType.ROZSZERZENIE);
        offer2.setLessonDateTime(LocalDateTime.now());
        offer2.setTutor(tutor2);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer1));
        when(offerRepository.save(offer1)).thenReturn(offer1);

        Optional<Offer> result = offerService.update(1L, offer2);

        assertTrue(result.isPresent());
        assertEquals("TestName2", result.get().getName());
        verify(offerRepository, times(1)).findById(1L);
        verify(offerRepository, times(1)).save(offer1);
    }

    @Test
    @DisplayName("Should delete an existing offer")
    public void testDeleteOffer() {
        offerService.delete(1L);
        verify(offerRepository, times(1)).deleteById(1L);
    }
}
