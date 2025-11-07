package org.moto.motravel.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "tour_packages")
public class TourPackage {
    @Id
    private String id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 4000)
    private String description;

    @Positive
    private int durationDays;

    @Size(max = 150)
    private String startingLocation;

    @Size(max = 150)
    private String endingLocation;

    @NotNull
    @Positive
    private BigDecimal basePricePerPerson;

    @NotNull
    @Positive
    private Integer maxGroupSize;

    private Set<String> highlights = new HashSet<>();

    private Set<String> imageUrls = new HashSet<>();

    private Set<LocalDate> availableDates = new HashSet<>();

    // Embedded itinerary items
    private List<ItineraryItem> itinerary = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String vendorId;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public String getStartingLocation() { return startingLocation; }
    public void setStartingLocation(String startingLocation) { this.startingLocation = startingLocation; }

    public String getEndingLocation() { return endingLocation; }
    public void setEndingLocation(String endingLocation) { this.endingLocation = endingLocation; }

    public BigDecimal getBasePricePerPerson() { return basePricePerPerson; }
    public void setBasePricePerPerson(BigDecimal basePricePerPerson) { this.basePricePerPerson = basePricePerPerson; }

    public Integer getMaxGroupSize() { return maxGroupSize; }
    public void setMaxGroupSize(Integer maxGroupSize) { this.maxGroupSize = maxGroupSize; }

    public Set<String> getHighlights() { return highlights; }
    public void setHighlights(Set<String> highlights) { this.highlights = highlights; }

    public Set<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(Set<String> imageUrls) { this.imageUrls = imageUrls; }

    public Set<LocalDate> getAvailableDates() { return availableDates; }
    public void setAvailableDates(Set<LocalDate> availableDates) { this.availableDates = availableDates; }

    public List<ItineraryItem> getItinerary() { return itinerary; }
    public void setItinerary(List<ItineraryItem> itinerary) { this.itinerary = itinerary; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
}
