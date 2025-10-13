package net.ensah.events;
import net.ensah.customerservice.enums.CustomerAction;


public record CustomerEvent(Long customerId,
                            CustomerAction action,
                            String email,
                            String firstName,
                            String lastName) { }
