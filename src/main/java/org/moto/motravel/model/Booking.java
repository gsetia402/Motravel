package org.moto.motravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    
    @NotNull
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @NotNull
    @Positive
    @Column(name = "total_price")
    private Double totalPrice;
    
    @NotNull
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
}
