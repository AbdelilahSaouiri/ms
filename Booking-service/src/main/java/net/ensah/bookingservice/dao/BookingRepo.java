package net.ensah.bookingservice.dao;

import net.ensah.bookingservice.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByCustomerId(Long customerId);

    List<BookingEntity> findByStatus(String status);

    List<Object> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
}
