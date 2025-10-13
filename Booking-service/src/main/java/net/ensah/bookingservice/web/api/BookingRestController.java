package net.ensah.bookingservice.web.api;

import net.ensah.bookingservice.service.IBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRestController {

    private final IBookingService service;
    public BookingRestController(IBookingService service1) {
        this.service = service1;
    }

    @GetMapping("/customer/events")
    public ResponseEntity<?> getCustomerEvents() {
        service.customerEventConsumer();
        return ResponseEntity.ok().build();
    }

}
