package com.brokerapp.controller.dto;


import com.brokerapp.enums.OrderSide;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderDto {
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private Long size;
    private Long price;
}
