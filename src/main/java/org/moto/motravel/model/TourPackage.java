package org.moto.motravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tour_packages")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 4000)
    @Column(length = 4000)
    private String description;

    @Positive
    @Column(name = "duration_days")
    private int durationDays;

    @Size(max = 150)
    @Column(name = "starting_location")
    private String startingLocation;

    @Size(max = 150)
    @Column(name = "ending_location")
    private String endingLocation;

    @NotNull
    @Positive
    @Column(name = "base_price_per_person")
    private BigDecimal basePricePerPerson;

    @NotNull
    @Positive
    @Column(name = "max_group_size")
    private Integer maxGroupSize;

    @ElementCollection
    @CollectionTable(name = "tour_package_highlights", joinColumns = @JoinColumn(name = "tour_package_id"))
    @Column(name = "highlight")
    private Set<String> highlights = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tour_package_images", joinColumns = @JoinColumn(name = "tour_package_id"))
    @Column(name = "image_url")
    private Set<String> imageUrls = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tour_package_available_dates", joinColumns = @JoinColumn(name = "tour_package_id"))
    @Column(name = "available_date")
    private Set<LocalDate> availableDates = new HashSet<>();

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("dayNumber ASC")
    private List<ItineraryItem> itinerary = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "vendor_id")
    private Long vendorId;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
}
