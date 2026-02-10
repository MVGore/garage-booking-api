package com.mvgore.garageapi.dto;

import com.mvgore.garageapi.entity.BookingStatus;

import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private String customerEmail;
    private Long garageId;
    private Long serviceId;
    private LocalDateTime bookingTime;
    private BookingStatus status;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Long getGarageId() { return garageId; }
    public void setGarageId(Long garageId) { this.garageId = garageId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
