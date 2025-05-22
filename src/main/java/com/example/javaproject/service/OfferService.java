package com.example.javaproject.service;

import com.example.javaproject.entity.Offer;
import com.example.javaproject.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAll() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getByID(Long id) {
        return offerRepository.findById(id);
    }

    public Offer create(Offer newOffer) {
        return offerRepository.save(newOffer);
    }

    public Optional<Offer> update(Long id, Offer updatedOffer) {
        return offerRepository.findById(id).map(offer -> {
            offer.setSubject(updatedOffer.getSubject());
            offer.setName(updatedOffer.getName());
            offer.setDescription(updatedOffer.getDescription());
            offer.setSchool_type(updatedOffer.getSchool_type());
            offer.setLevel_type(updatedOffer.getLevel_type());
            offer.setLessonDateTime(updatedOffer.getLessonDateTime());
            offer.setTutor(updatedOffer.getTutor());
            return offerRepository.save(offer);
        });
    }

    public void delete(Long id) {
        offerRepository.deleteById(id);
    }
}