package net.ensah.bookingservice.service.Impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.ensah.bookingservice.dao.BookingRepo;
import net.ensah.bookingservice.dto.requests.BookingRequest;
import net.ensah.bookingservice.dto.response.BookingResponseDto;
import net.ensah.bookingservice.entities.BookingEntity;
import net.ensah.bookingservice.enums.BookingAction;
import net.ensah.bookingservice.enums.BookingStatus;
import net.ensah.bookingservice.service.IBookingService;
import net.ensah.customerservice.enums.CustomerAction;
import net.ensah.events.BookingEvent;
import net.ensah.events.CustomerEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
@Slf4j
public class BookingServiceImpl implements IBookingService {

    private final BookingRepo bookingRepository;
    private final StreamBridge streamBridge;

    public BookingServiceImpl(BookingRepo bookingRepository, StreamBridge streamBridge) {
        this.bookingRepository = bookingRepository;
        this.streamBridge = streamBridge;
    }


    @Override
    public BookingResponseDto createBooking(BookingRequest request) {
        BookingEntity entity = new BookingEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setStatus(BookingStatus.CONFIRMED);
        BookingEntity saved = bookingRepository.save(entity);
        BookingResponseDto response = mapToDto(saved);
        publishBookingEvent(new BookingEvent(saved.getId(),
                BookingAction.CREATED,
                saved.getCustomerId(),
                saved.getStatus().toString(),
                saved.getPrice()));
        return response;
    }

    @Override
    public Optional<BookingResponseDto> getBookingById(Long id) {
        return bookingRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public BookingResponseDto updateBooking(Long id, BookingRequest request) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        BeanUtils.copyProperties(request, entity, "id", "status");
        BookingEntity updated = bookingRepository.save(entity);

        BookingResponseDto response = mapToDto(updated);
        publishBookingEvent(new BookingEvent(updated.getId(), BookingAction.UPDATED, updated.getCustomerId(), updated.getStatus().toString(), updated.getPrice()));
        return response;
    }

    @Override
    public void cancelBooking(Long id) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        entity.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(entity);

        publishBookingEvent(new BookingEvent(entity.getId(), BookingAction.CANCELLED, entity.getCustomerId(), entity.getStatus().toString(), entity.getPrice()));
    }

    @Override
    public List<BookingResponseDto> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream().map(this::mapToDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status).stream().map(this::mapToDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsBetweenDates(LocalDateTime start, LocalDateTime end) {
        //return bookingRepository.findByStartDateBetween(start, end).stream().map(this::mapToDto).toList();
        return List.of();
    }

    @Override
    public void publishBookingEvent(BookingEvent event) {
        log.info("Publishing booking event: {}", event);
        streamBridge.send("booking-out-0", event);
    }

    @Override
    public Consumer<CustomerEvent> customerEventConsumer() {
        return event -> {
            log.info("Received CustomerEvent in BookingService: {}", event);
            if (event.action() == CustomerAction.DELETED) {
                log.warn("Cancelling all bookings for deleted customerId={}", event.customerId());
                bookingRepository.findByCustomerId(event.customerId())
                        .forEach(booking -> {
                            booking.setStatus(BookingStatus.CANCELLED);
                            bookingRepository.save(booking);
                            publishBookingEvent(new BookingEvent(
                                    booking.getId(),
                                    BookingAction.CANCELLED,
                                    booking.getCustomerId(),
                                    booking.getStatus().toString(),
                                    booking.getPrice()
                            ));
                        });
            }
        };
    }

    private BookingResponseDto mapToDto(BookingEntity entity) {
        return new BookingResponseDto(
                entity.getId(),
                entity.getCustomerId(),
                entity.getServiceType(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                entity.getStatus().toString()
        );
    }
}