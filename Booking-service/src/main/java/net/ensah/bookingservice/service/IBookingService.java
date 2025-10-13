package net.ensah.bookingservice.service;

import net.ensah.bookingservice.dto.requests.BookingRequest;
import net.ensah.bookingservice.dto.response.BookingResponseDto;
import net.ensah.events.BookingEvent;
import net.ensah.events.CustomerEvent;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface IBookingService {

    BookingResponseDto createBooking(BookingRequest booking);

    Optional<BookingResponseDto> getBookingById(Long id);

    List<BookingResponseDto> getAllBookings();

    BookingResponseDto updateBooking(Long id, BookingRequest booking);

    void cancelBooking(Long id);

    List<BookingResponseDto> getBookingsByCustomerId(Long customerId);

    List<BookingResponseDto> getBookingsByStatus(String status);

    List<BookingResponseDto> getBookingsBetweenDates(LocalDateTime start, LocalDateTime end);

    // Publier un événement quand une réservation est faite ou annulée
    void publishBookingEvent(BookingEvent event);

   Consumer<CustomerEvent>  customerEventConsumer();

}
