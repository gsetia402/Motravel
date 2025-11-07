package org.moto.motravel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Document(collection = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    private String id;

    @NotBlank
    private String model;

    @NotBlank
    private String brand;

    @NotBlank
    private String type; // car/bike

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @GeoSpatialIndexed(type = org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location; // derived from latitude/longitude

    @NotNull
    @Positive
    private Double hourlyPrice;

    private String imageUrl;

    @NotNull
    private Boolean availability = true;

    private String vendorId;
}
