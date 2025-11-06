package org.moto.motravel.service;

import org.moto.motravel.model.TourBooking;
import org.moto.motravel.model.TourPackage;
import org.moto.motravel.repository.TourBookingRepository;
import org.moto.motravel.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TourService {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private TourBookingRepository tourBookingRepository;

    public List<TourPackage> getAllTours() {
        return tourPackageRepository.findAll();
    }

    public TourPackage getTourById(Long id) {
        return tourPackageRepository.findById(id).orElse(null);
    }

    public boolean checkAvailability(Long tourId, LocalDate date, int guests) {
        TourPackage tour = getTourById(tourId);
        if (tour == null) return false;
        if (!tour.getAvailableDates().contains(date)) return false;
        int booked = tourBookingRepository.getBookedCountForDate(tourId, date);
        return booked + guests <= tour.getMaxGroupSize();
    }

    public BigDecimal calculatePrice(TourPackage tour, int adults, int children) {
        BigDecimal base = tour.getBasePricePerPerson();
        BigDecimal adultTotal = base.multiply(BigDecimal.valueOf(adults));
        BigDecimal childTotal = base.multiply(BigDecimal.valueOf(0.5)).multiply(BigDecimal.valueOf(children));
        return adultTotal.add(childTotal).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public TourBooking createBooking(Long tourId, LocalDate date, int adults, int children,
                                     String contactName, String contactEmail, String contactPhone,
                                     Long userId) {
        TourPackage tour = getTourById(tourId);
        if (tour == null) throw new IllegalArgumentException("Tour not found");
        int guests = adults + children;
        if (guests <= 0) throw new IllegalArgumentException("Guests must be greater than 0");
        if (!checkAvailability(tourId, date, guests)) {
            throw new IllegalStateException("Selected date is not available or group size exceeds availability");
        }
        TourBooking booking = new TourBooking();
        booking.setTourPackage(tour);
        booking.setDate(date);
        booking.setAdults(adults);
        booking.setChildren(children);
        booking.setTotalPrice(calculatePrice(tour, adults, children));
        booking.setContactName(contactName);
        booking.setContactEmail(contactEmail);
        booking.setContactPhone(contactPhone);
        booking.setStatus("CONFIRMED");
        booking.setBookingId(generateBookingId());
        if (userId != null) {
            booking.setUserId(userId);
        }
        // Set vendorId from tour package
        booking.setVendorId(tour.getVendorId());
        return tourBookingRepository.save(booking);
    }

    public java.util.List<TourBooking> getBookingsByUserId(Long userId) {
        return tourBookingRepository.findByUserId(userId);
    }

    private String generateBookingId() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        String id = "TP-" + date + "-" + sb;
        // ensure uniqueness (very unlikely to collide)
        if (tourBookingRepository.existsByBookingId(id)) return generateBookingId();
        return id;
    }
}
