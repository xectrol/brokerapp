package com.brokerapp.controller;


import com.brokerapp.controller.dto.CustomerDto;
import com.brokerapp.controller.dto.DepositDto;
import com.brokerapp.controller.dto.WithdrawDto;
import com.brokerapp.controller.resource.CustomerResource;
import com.brokerapp.mapper.CustomerMapper;
import com.brokerapp.model.Customer;
import com.brokerapp.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping
    public CustomerResource registerCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.registerCustomer(customerMapper.customerCommandToCustomer(customerDto));
        return customerMapper.customerToCustomerResource(customer);
    }

    @GetMapping("/me")
    public CustomerResource getCurrentCustomer(UsernamePasswordAuthenticationToken principal) {
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        return customerMapper.customerToCustomerResource(customer);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerResource> listAllCustomers() {
        List<Customer> customers = customerService.listAllCustomers();
        return customers.stream().map(customerMapper::customerToCustomerResource).toList();
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ADMIN')")
    public void depositMoney(@RequestBody DepositDto depositDto) {
        customerService.depositMoney(customerMapper.depositCommandToDeposit(depositDto));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('ADMIN')")
    public void withdrawMoney(@RequestBody WithdrawDto withdrawDto) {
        customerService.withdrawMoney(customerMapper.withdrawCommandToDeposit(withdrawDto));
    }
}
