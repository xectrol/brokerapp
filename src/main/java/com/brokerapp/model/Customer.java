package com.brokerapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Long amount;
}
