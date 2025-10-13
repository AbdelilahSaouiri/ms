package net.ensah.events;

import net.ensah.bookingservice.enums.BookingAction;

public record BookingEvent(Long bookingId,
                           BookingAction action,
                           Long customerId,
                           String  status,
                           Double price


) { }
