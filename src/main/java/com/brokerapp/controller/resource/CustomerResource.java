package com.brokerapp.controller.resource;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerResource {
    private Long id;
    private String username;
    private String password;
    private String role;
}
