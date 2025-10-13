package net.ensah.customerservice.dto.requests;

import java.time.LocalDate;

public record CustomerRequestDto(String firstName,
                                 String lastName,
                                 String email,
                                 String phoneNumber,
                                 LocalDate dateOfBirth,
                                 String address
                                 ) {
}
