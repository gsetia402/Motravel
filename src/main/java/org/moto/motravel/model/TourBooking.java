package org.moto.motravel.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

@Document(collection = "tour_bookings")
public class TourBooking {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    private String bookingId;

    @NotBlank
    private String tourPackageId;

    @NotNull
    private LocalDate date;

    @Min(1)
    private int adults;

    @Min(0)
    private int children;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    @NotBlank
    private String contactName;

    @NotBlank
    @Email
    private String contactEmail;

    @NotBlank
    private String contactPhone;

    @NotBlank
    private String status; // PENDING, CONFIRMED, CANCELLED

    private String userId; // optional

    private String vendorId;

    @CreatedDate
    private Instant createdAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getTourPackageId() { return tourPackageId; }
    public void setTourPackageId(String tourPackageId) { this.tourPackageId = tourPackageId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getAdults() { return adults; }
    public void setAdults(int adults) { this.adults = adults; }

    public int getChildren() { return children; }
    public void setChildren(int children) { this.children = children; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
