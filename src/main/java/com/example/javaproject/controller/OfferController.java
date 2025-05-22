package com.example.javaproject.controller;

import com.example.javaproject.entity.Offer;
import com.example.javaproject.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Offer Controller", description="Offers management")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @Operation(summary="Get all offers", description="Receives all offers posted by tutors")
    @GetMapping("/offers/get/all")
    public List<Offer> getAll() {
        return offerService.getAll();
    }

    @Operation(summary="Get offer by id", description="Receives offer with provided id")
    @GetMapping("/offers/get/{id}")
    public Optional<Offer> getByID(
            @Parameter(description="Offer id") @PathVariable Long id
    ) {
        return offerService.getByID(id);
    }

    @Operation(summary="Creates new offer", description="Creates new offer of a tutor")
    @PostMapping("/offers/create")
    public Offer create(@RequestBody Offer newOffer) {
        return offerService.create(newOffer);
    }

    @Operation(summary="Updates existing offer", description="Updates a offer with the given id")
    @PutMapping("/offers/update/{id}")
    public Optional<Offer> update(
            @Parameter(description = "Offer id") @PathVariable Long id,
            @RequestBody Offer updatedOffer
    ) {
        return offerService.update(id, updatedOffer);
    }

    @Operation(summary="Deletes offer by id", description="Deletes the offer with the given id")
    @DeleteMapping("/offers/delete/{id}")
    public void delete(
            @Parameter(description = "Offer id") @PathVariable Long id
    ) {
        offerService.delete(id);
    }
}