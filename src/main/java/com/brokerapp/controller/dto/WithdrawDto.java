package com.brokerapp.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawDto {
    private Long customerId;
    private Long amount;
    private String iban;
    private Long version;
}
