package org.moto.motravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hidden_gems")
public class HiddenGem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hidden gem name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    @NotNull(message = "State is required")
    private State state;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "hidden_gem_adventure_types",
        joinColumns = @JoinColumn(name = "hidden_gem_id"),
        inverseJoinColumns = @JoinColumn(name = "adventure_type_id")
    )
    private Set<AdventureType> adventureTypes = new HashSet<>();

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(nullable = false)
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(nullable = false)
    private Double longitude;

    @Size(max = 100, message = "Nearest city must not exceed 100 characters")
    @Column(name = "nearest_city")
    private String nearestCity;

    @Size(max = 100, message = "Best time to visit must not exceed 100 characters")
    @Column(name = "best_time_to_visit")
    private String bestTimeToVisit;

    @Size(max = 50, message = "Difficulty level must not exceed 50 characters")
    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Size(max = 100, message = "Cost range must not exceed 100 characters")
    @Column(name = "cost_range")
    private String costRange;

    @ElementCollection
    @CollectionTable(name = "hidden_gem_images", joinColumns = @JoinColumn(name = "hidden_gem_id"))
    @Column(name = "image_url")
    private Set<String> imageUrls = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public HiddenGem() {}

    public HiddenGem(String name, String description, State state, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<AdventureType> getAdventureTypes() {
        return adventureTypes;
    }

    public void setAdventureTypes(Set<AdventureType> adventureTypes) {
        this.adventureTypes = adventureTypes;
    }

    public void addAdventureType(AdventureType adventureType) {
        this.adventureTypes.add(adventureType);
        adventureType.getHiddenGems().add(this);
    }

    public void removeAdventureType(AdventureType adventureType) {
        this.adventureTypes.remove(adventureType);
        adventureType.getHiddenGems().remove(this);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNearestCity() {
        return nearestCity;
    }

    public void setNearestCity(String nearestCity) {
        this.nearestCity = nearestCity;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getCostRange() {
        return costRange;
    }

    public void setCostRange(String costRange) {
        this.costRange = costRange;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HiddenGem)) return false;
        HiddenGem hiddenGem = (HiddenGem) o;
        return id != null && id.equals(hiddenGem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "HiddenGem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + (state != null ? state.getName() : null) +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nearestCity='" + nearestCity + '\'' +
                '}';
    }
}
