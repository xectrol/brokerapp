package com.brokerapp.service.impl;


import com.brokerapp.exception.BadRequestException;
import com.brokerapp.exception.InsufficientBalanceException;
import com.brokerapp.exception.NotFoundException;
import com.brokerapp.mapper.CustomerMapper;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;
import com.brokerapp.persistence.CustomerRepository;
import com.brokerapp.persistence.entity.CustomerEntity;
import com.brokerapp.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;

    @Transactional
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already exist");
        }

        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        CustomerEntity customerEntity = CustomerEntity.builder().username(customer.getUsername()).password(encodedPassword).role("ROLE_USER").amount(0L).build();

        return customerMapper.customerEntityToCustomer(customerRepository.save(customerEntity));
    }

    @Transactional
    public void depositMoney(Deposit deposit) {
        if (deposit.getAmount().compareTo(0L) <= 0) {
            throw new InsufficientBalanceException("Deposit money should be positive");
        }

        Optional<CustomerEntity> customer = customerRepository.findById(deposit.getCustomerId());
        if (customer.isPresent()) {
            customer.get().setAmount(deposit.getAmount() + customer.get().getAmount());
        }else {
            throw new NotFoundException("Customer not found");
        }
        customerRepository.save(customer.get());
    }


    public void withdrawMoney(Withdraw withdraw) {
        if (withdraw.getAmount().compareTo(0L) <= 0) {
            throw new IllegalArgumentException("Withdraw money should be positive");
        }

        Optional<CustomerEntity> customer = customerRepository.findById(withdraw.getCustomerId());
        if (customer.isPresent()) {

            if (customer.get().getAmount() < withdraw.getAmount()){
                throw new BadRequestException("Customer amount can not be smaller than withdraw amount");
            }else  {
                customer.get().setAmount(customer.get().getAmount() - withdraw.getAmount());
            }
        }else {
            throw new NotFoundException("Customer not found");
        }
    }

    public Customer findByUsername(String username) {
        return customerMapper.customerEntityToCustomer(customerRepository.findByUsername(username).orElse(null));
    }

    public List<Customer> listAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerEntityToCustomer
        ).toList();
    }
}
