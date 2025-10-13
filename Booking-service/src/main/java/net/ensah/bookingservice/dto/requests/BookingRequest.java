package net.ensah.bookingservice.dto.requests;

import java.time.LocalDateTime;

public record BookingRequest(
        Long customerId,
        String serviceType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double price
) {}