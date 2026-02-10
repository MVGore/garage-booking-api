package com.mvgore.garageapi.dto;

import com.mvgore.garageapi.entity.BookingStatus;

public class BookingResponse {
    private Long bookingId;
    private BookingStatus status;

    // Getters/Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
