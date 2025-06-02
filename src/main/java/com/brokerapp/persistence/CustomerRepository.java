package com.brokerapp.persistence;


import com.brokerapp.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUsername(String username);
    Optional<CustomerEntity> findById(Long id);

}

