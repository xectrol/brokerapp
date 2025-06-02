package com.brokerapp.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositDto {
    private Long customerId;
    private Long amount;
    private Long version;
}
