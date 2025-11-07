package org.moto.motravel.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String companyName;

    @Email
    private String email;

    private String contactPhone;

    // Vendor department: TOUR (tour packages) or VEHICLE (vehicle rentals)
    private String department; // "TOUR" or "VEHICLE"

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private String rejectionReason;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
