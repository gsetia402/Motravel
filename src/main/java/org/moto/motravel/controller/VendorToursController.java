package org.moto.motravel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moto.motravel.model.TourPackage;
import org.moto.motravel.model.User;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.repository.TourPackageRepository;
import org.moto.motravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vendor/tours")
@Tag(name = "Vendor - Tours", description = "Vendor APIs to manage own tour packages")
@PreAuthorize("hasAnyAuthority('ROLE_VENDOR','ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class VendorToursController {

    @Autowired private TourPackageRepository tourPackageRepository;
    @Autowired private UserRepository userRepository;

    private Long currentVendorId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).map(User::getVendorId).orElse(null);
    }

    @GetMapping
    @Operation(summary = "List my tour packages (vendor) or by vendorId (admin)")
    public ResponseEntity<List<TourPackage>> list(@RequestParam(required = false) Long vendorId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        Long vid = isAdmin ? (vendorId != null ? vendorId : currentVendorId()) : currentVendorId();
        if (vid == null && !isAdmin) return ResponseEntity.status(403).<List<TourPackage>>build();
        List<TourPackage> list = (vid == null) ? tourPackageRepository.findAll() : tourPackageRepository.findByVendorId(vid);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Operation(summary = "Create a tour package for current vendor")
    public ResponseEntity<?> create(@Valid @RequestBody TourPackage tour) {
        Long vid = currentVendorId();
        if (vid == null) return ResponseEntity.status(403).body(new MessageResponse("No vendor assigned to user"));
        tour.setVendorId(vid);
        TourPackage saved = tourPackageRepository.save(tour);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update my tour package")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TourPackage updates) {
        Long vid = currentVendorId();
        var opt = tourPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Tour package not found or not owned by vendor"));
        }
        var t = opt.get();
        if (t.getVendorId() == null || !t.getVendorId().equals(vid)) {
            return ResponseEntity.status(404).body(new MessageResponse("Tour package not found or not owned by vendor"));
        }
        t.setName(updates.getName());
        t.setDescription(updates.getDescription());
        t.setDurationDays(updates.getDurationDays());
        t.setStartingLocation(updates.getStartingLocation());
        t.setEndingLocation(updates.getEndingLocation());
        t.setBasePricePerPerson(updates.getBasePricePerPerson());
        t.setMaxGroupSize(updates.getMaxGroupSize());
        t.setHighlights(updates.getHighlights());
        t.setImageUrls(updates.getImageUrls());
        t.setAvailableDates(updates.getAvailableDates());
        t.setItinerary(updates.getItinerary());
        return ResponseEntity.ok(tourPackageRepository.save(t));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete my tour package")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long vid = currentVendorId();
        var opt = tourPackageRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(new MessageResponse("Tour package not found or not owned by vendor"));
        }
        var t = opt.get();
        if (t.getVendorId() == null || !t.getVendorId().equals(vid)) {
            return ResponseEntity.status(404).body(new MessageResponse("Tour package not found or not owned by vendor"));
        }
        tourPackageRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Tour package deleted"));
    }
}
