package com.brokerapp.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    private String username;
    private String password;
    private String role;
    private Long amount;
}
