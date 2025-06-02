package com.brokerapp.service;



import com.brokerapp.model.Customer;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;

import java.util.List;

public interface ICustomerService {
    Customer registerCustomer(Customer customer);
    Customer findByUsername(String username);
    List<Customer> listAllCustomers();
    void depositMoney(Deposit deposit);
    void withdrawMoney(Withdraw withdraw);

}
