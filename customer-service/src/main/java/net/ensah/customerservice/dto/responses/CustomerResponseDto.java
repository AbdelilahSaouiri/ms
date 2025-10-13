package net.ensah.customerservice.dto.responses;

import java.time.LocalDate;

public record CustomerResponseDto(Long id, String firstName,
                                  String lastName,
                                  String email,
                                  String phoneNumber,
                                  LocalDate dateOfBirth,
                                  String address
                                 ) {
}
