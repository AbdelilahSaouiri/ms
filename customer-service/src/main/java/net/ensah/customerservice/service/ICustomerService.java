package net.ensah.customerservice.service;
import net.ensah.events.CustomerEvent;
import net.ensah.customerservice.dto.requests.CustomerRequestDto;
import net.ensah.customerservice.dto.responses.CustomerResponseDto;
import java.util.List;

public interface ICustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto);
    void deleteCustomer(Long customerId);
    CustomerResponseDto getCustomerById(Long customerId);
    List<CustomerResponseDto> getAllCustomers();
    void publishCustomerEvent(CustomerEvent event);

}
