package net.ensah.customerservice.service.Impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.ensah.customerservice.dao.CustomerRepository;
import net.ensah.customerservice.dto.requests.CustomerRequestDto;
import net.ensah.customerservice.dto.responses.CustomerResponseDto;
import net.ensah.customerservice.entity.CustomerEntity;
import net.ensah.customerservice.enums.CustomerAction;
import net.ensah.customerservice.service.ICustomerService;
import net.ensah.events.CustomerEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

    public CustomerServiceImpl(CustomerRepository customerRepository, StreamBridge streamBridge) {
        this.customerRepository = customerRepository;
        this.streamBridge = streamBridge;
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {
        CustomerEntity entity = new CustomerEntity();
        BeanUtils.copyProperties(dto, entity);
        CustomerEntity saved = customerRepository.save(entity);

        CustomerResponseDto response = mapToDto(saved);
        publishCustomerEvent(new CustomerEvent(saved.getId(), CustomerAction.CREATED,
                saved.getEmail(), saved.getFirstName(), saved.getLastName()));
        return response;
    }

    @Override
    public CustomerResponseDto updateCustomer(CustomerRequestDto dto) {
        CustomerEntity entity = customerRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + dto.email()));

        BeanUtils.copyProperties(dto, entity, "id");
        CustomerEntity updated = customerRepository.save(entity);

        CustomerResponseDto response = mapToDto(updated);
        publishCustomerEvent(new CustomerEvent(updated.getId(), CustomerAction.UPDATED,
                updated.getEmail(), updated.getFirstName(), updated.getLastName()));
        return response;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
        publishCustomerEvent(new CustomerEvent(customerId, CustomerAction.DELETED, null, null, null));
    }

    @Override
    public CustomerResponseDto getCustomerById(Long customerId) {
        CustomerEntity entity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return mapToDto(entity);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public void publishCustomerEvent(CustomerEvent event) {
        log.info("Publishing customer event: {}", event);
        streamBridge.send("customer-out-0", event);
    }

    private CustomerResponseDto mapToDto(CustomerEntity entity) {
        return new CustomerResponseDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getDateOfBirth(),
                entity.getAddress()
        );
    }
}