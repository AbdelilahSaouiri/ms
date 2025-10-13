package net.ensah.customerservice.dao;

import net.ensah.customerservice.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerRepository  extends JpaRepository<CustomerEntity,Long> {
   Optional<CustomerEntity> findByEmail(String email);
}
