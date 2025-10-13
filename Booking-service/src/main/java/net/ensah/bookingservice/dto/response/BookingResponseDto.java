package net.ensah.bookingservice.dto.response;

import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        Long customerId,
        String serviceType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double price,
        String status
) {}
