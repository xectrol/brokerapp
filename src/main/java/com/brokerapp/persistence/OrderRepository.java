package com.brokerapp.persistence;


import com.brokerapp.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
}

