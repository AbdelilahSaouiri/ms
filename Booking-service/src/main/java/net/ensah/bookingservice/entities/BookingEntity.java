package net.ensah.bookingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.ensah.bookingservice.enums.BookingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@AllArgsConstructor @NoArgsConstructor @Builder @Getter
@Setter
public class BookingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId; // référence au client (via ID, pas FK)

    @Column(nullable = false)
    private String serviceType; // ex: "hotel", "flight"

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // ex: "CONFIRMED", "CANCELLED"

}
