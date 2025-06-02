package com.brokerapp.persistence.entity;


import com.brokerapp.enums.OrderSide;
import com.brokerapp.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String assetName;

    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private Long size;
    private Long price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime createDate;
}
