package com.brokerapp.controller.resource;


import com.brokerapp.enums.OrderSide;
import com.brokerapp.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResource {
    private Long id;
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private Long size;
    private Long price;
    private OrderStatus status;
    private LocalDateTime createDate;
}
