package com.brokerapp.controller;

import com.brokerapp.controller.CustomerController;
import com.brokerapp.controller.dto.CustomerDto;
import com.brokerapp.controller.dto.DepositDto;
import com.brokerapp.controller.dto.WithdrawDto;
import com.brokerapp.controller.resource.CustomerResource;
import com.brokerapp.mapper.CustomerMapper;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;
import com.brokerapp.service.ICustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private ICustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    private UsernamePasswordAuthenticationToken principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = new UsernamePasswordAuthenticationToken("user1", null, List.of());
    }

    @Test
    void registerCustomer_shouldReturnCustomerResource() {
        CustomerDto dto = CustomerDto.builder().username("user1").password("pass").build();
        Customer model = Customer.builder().id(1L).username("user1").build();
        CustomerResource resource = CustomerResource.builder().id(1L).username("user1").build();

        when(customerMapper.customerCommandToCustomer(dto)).thenReturn(model);
        when(customerService.registerCustomer(model)).thenReturn(model);
        when(customerMapper.customerToCustomerResource(model)).thenReturn(resource);

        CustomerResource result = customerController.registerCustomer(dto);

        assertEquals("user1", result.getUsername());
    }

    @Test
    void getCurrentCustomer_shouldReturnCustomerResource() {
        Customer model = Customer.builder().id(1L).username("user1").build();
        CustomerResource resource = CustomerResource.builder().id(1L).username("user1").build();

        when(customerService.findByUsername("user1")).thenReturn(model);
        when(customerMapper.customerToCustomerResource(model)).thenReturn(resource);

        CustomerResource result = customerController.getCurrentCustomer(principal);

        assertEquals("user1", result.getUsername());
    }

    @Test
    void getCurrentCustomer_shouldThrowIfNotFound() {
        when(customerService.findByUsername("user1")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> customerController.getCurrentCustomer(principal));
    }

    @Test
    void listAllCustomers_shouldReturnList() {
        Customer model = Customer.builder().id(1L).username("admin").build();
        CustomerResource resource = CustomerResource.builder().id(1L).username("admin").build();

        when(customerService.listAllCustomers()).thenReturn(List.of(model));
        when(customerMapper.customerToCustomerResource(model)).thenReturn(resource);

        List<CustomerResource> result = customerController.listAllCustomers();

        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUsername());
    }

    @Test
    void depositMoney_shouldCallService() {
        DepositDto dto = DepositDto.builder().amount(1000L).customerId(1L).build();
        Deposit model = Deposit.builder().amount(1000L).customerId(1L).build();

        when(customerMapper.depositCommandToDeposit(dto)).thenReturn(model);

        customerController.depositMoney(dto);

        verify(customerService).depositMoney(model);
    }

    @Test
    void withdrawMoney_shouldCallService() {
        WithdrawDto dto = WithdrawDto.builder().amount(500L).customerId(1L).build();
        Withdraw model = Withdraw.builder().amount(500L).customerId(1L).build();

        when(customerMapper.withdrawCommandToDeposit(dto)).thenReturn(model);

        customerController.withdrawMoney(dto);

        verify(customerService).withdrawMoney(model);
    }
}
