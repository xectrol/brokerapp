package com.brokerapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Withdraw {
    private Long customerId;
    private Long amount;
    private String iban;
    private Long version;
}
