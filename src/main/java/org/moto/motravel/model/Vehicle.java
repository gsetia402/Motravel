package org.moto.motravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String model;
    
    @NotBlank
    private String brand;
    
    @NotBlank
    @Column(name = "vehicle_type")
    private String type; // car/bike
    
    @NotNull
    private Double latitude;
    
    @NotNull
    private Double longitude;
    
    @NotNull
    @Positive
    private Double hourlyPrice;
    
    private String imageUrl;
    
    @NotNull
    private Boolean availability = true;
}
