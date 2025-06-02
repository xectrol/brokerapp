package com.brokerapp.service;

import com.brokerapp.exception.BadRequestException;
import com.brokerapp.exception.InsufficientBalanceException;
import com.brokerapp.exception.NotFoundException;
import com.brokerapp.mapper.CustomerMapper;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;
import com.brokerapp.persistence.CustomerRepository;
import com.brokerapp.persistence.entity.CustomerEntity;
import com.brokerapp.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCustomer_success() {
        Customer customer = Customer.builder().username("test").password("123").build();
        CustomerEntity savedEntity = CustomerEntity.builder().username("test").password("encoded").role("ROLE_USER").amount(0L).build();
        Customer mappedCustomer = Customer.builder().username("test").build();

        when(customerRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(customerRepository.save(any())).thenReturn(savedEntity);
        when(customerMapper.customerEntityToCustomer(savedEntity)).thenReturn(mappedCustomer);

        Customer result = customerService.registerCustomer(customer);

        assertEquals("test", result.getUsername());
    }

    @Test
    void registerCustomer_shouldThrow_whenUsernameExists() {
        Customer customer = Customer.builder().username("test").build();
        when(customerRepository.findByUsername("test")).thenReturn(Optional.of(new CustomerEntity()));

        assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(customer));
    }

    @Test
    void depositMoney_success() {
        Deposit deposit = new Deposit();
        deposit.setAmount(100L);
        deposit.setCustomerId(1L);

        CustomerEntity entity = CustomerEntity.builder().id(1L).amount(200L).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));

        customerService.depositMoney(deposit);

        assertEquals(300L, entity.getAmount());
        verify(customerRepository).save(entity);
    }

    @Test
    void depositMoney_shouldThrow_whenAmountIsZero() {
        Deposit deposit = new Deposit();
        deposit.setAmount(0L);

        assertThrows(InsufficientBalanceException.class, () -> customerService.depositMoney(deposit));
    }

    @Test
    void depositMoney_shouldThrow_whenCustomerNotFound() {
        Deposit deposit = new Deposit();
        deposit.setAmount(100L);
        deposit.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.depositMoney(deposit));
    }

    @Test
    void withdrawMoney_success() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(100L);
        withdraw.setCustomerId(1L);

        CustomerEntity entity = CustomerEntity.builder().id(1L).amount(200L).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));

        customerService.withdrawMoney(withdraw);

        assertEquals(100L, entity.getAmount());
    }

    @Test
    void withdrawMoney_shouldThrow_whenAmountInvalid() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(0L);

        assertThrows(IllegalArgumentException.class, () -> customerService.withdrawMoney(withdraw));
    }

    @Test
    void withdrawMoney_shouldThrow_whenCustomerNotFound() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(100L);
        withdraw.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.withdrawMoney(withdraw));
    }

    @Test
    void withdrawMoney_shouldThrow_whenInsufficientBalance() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(300L);
        withdraw.setCustomerId(1L);

        CustomerEntity entity = CustomerEntity.builder().id(1L).amount(200L).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(BadRequestException.class, () -> customerService.withdrawMoney(withdraw));
    }

    @Test
    void findByUsername_shouldReturnMappedCustomer() {
        CustomerEntity entity = CustomerEntity.builder().username("user1").build();
        Customer customer = Customer.builder().username("user1").build();

        when(customerRepository.findByUsername("user1")).thenReturn(Optional.of(entity));
        when(customerMapper.customerEntityToCustomer(entity)).thenReturn(customer);

        Customer result = customerService.findByUsername("user1");

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void findByUsername_shouldReturnNull_ifNotFound() {
        when(customerRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(customerMapper.customerEntityToCustomer(null)).thenReturn(null);

        Customer result = customerService.findByUsername("user1");

        assertNull(result);
    }

    @Test
    void listAllCustomers_shouldReturnMappedList() {
        CustomerEntity entity = CustomerEntity.builder().username("user1").build();
        Customer customer = Customer.builder().username("user1").build();

        when(customerRepository.findAll()).thenReturn(List.of(entity));
        when(customerMapper.customerEntityToCustomer(entity)).thenReturn(customer);

        List<Customer> result = customerService.listAllCustomers();

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
    }
}
