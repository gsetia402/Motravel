package org.moto.motravel.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "hidden_gems")
public class HiddenGem {
    @Id
    private String id;

    @NotBlank(message = "Hidden gem name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    @Indexed
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank
    private String stateId;

    private Set<String> adventureTypeIds = new HashSet<>();

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @GeoSpatialIndexed(type = org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location; // derived from longitude, latitude

    @Size(max = 100)
    private String nearestCity;

    @Size(max = 100)
    private String bestTimeToVisit;

    @Size(max = 50)
    private String difficultyLevel;

    @Size(max = 100)
    private String costRange;

    private Set<String> imageUrls = new HashSet<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public Set<String> getAdventureTypeIds() { return adventureTypeIds; }
    public void setAdventureTypeIds(Set<String> adventureTypeIds) { this.adventureTypeIds = adventureTypeIds; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public GeoJsonPoint getLocation() { return location; }
    public void setLocation(GeoJsonPoint location) { this.location = location; }
    public String getNearestCity() { return nearestCity; }
    public void setNearestCity(String nearestCity) { this.nearestCity = nearestCity; }
    public String getBestTimeToVisit() { return bestTimeToVisit; }
    public void setBestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getCostRange() { return costRange; }
    public void setCostRange(String costRange) { this.costRange = costRange; }
    public Set<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(Set<String> imageUrls) { this.imageUrls = imageUrls; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
