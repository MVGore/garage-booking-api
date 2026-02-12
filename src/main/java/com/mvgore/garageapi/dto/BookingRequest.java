package com.mvgore.garageapi.dto;

import java.time.LocalDateTime;

public class BookingRequest {
    // private String customerEmail;
    private Long garageId;
    private Long serviceId;
    private LocalDateTime bookingTime;

    // Getters/Setters
    // public String getCustomerEmail() { return customerEmail; }
    // public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Long getGarageId() { return garageId; }
    public void setGarageId(Long garageId) { this.garageId = garageId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
}
