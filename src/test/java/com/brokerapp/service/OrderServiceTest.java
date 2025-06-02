package com.brokerapp.service;

import com.brokerapp.enums.OrderSide;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.mapper.OrderMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Order;
import com.brokerapp.persistence.AssetRepository;
import com.brokerapp.persistence.CustomerRepository;
import com.brokerapp.persistence.OrderRepository;
import com.brokerapp.persistence.entity.CustomerEntity;
import com.brokerapp.persistence.entity.OrderEntity;
import com.brokerapp.service.impl.AssetService;
import com.brokerapp.service.impl.CustomerService;
import com.brokerapp.service.impl.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private AssetService assetService;
    @Mock private OrderMapper orderMapper;
    @Mock private AssetMapper assetMapper;
    @Mock private CustomerService customerService;
    @Mock private AssetRepository assetRepository;

    @InjectMocks private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_sell_success() {
        Order order = Order.builder()
                .orderSide(OrderSide.SELL)
                .customerId(1L)
                .assetName("BTC")
                .size(10L)
                .build();

        Asset asset = Asset.builder()
                .assetName("BTC")
                .customerId(1L)
                .usableSize(20L)
                .size(20L)
                .build();

        when(assetService.assetsByCustomerIdAndAssetName(1L, "BTC")).thenReturn(asset);
        when(orderMapper.orderToOrderEntity(any())).thenReturn(new OrderEntity());
        when(orderRepository.save(any())).thenReturn(new OrderEntity());
        when(orderMapper.orderEntityToOrder(any())).thenReturn(order);
        when(assetMapper.assetToAssetEntity(any())).thenReturn(null); // gerekli çünkü assetRepository.save() çağrılıyor

        Order result = orderService.createOrder(order, mockPrincipal());

        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void createOrder_buy_success() {
        Order order = Order.builder()
                .orderSide(OrderSide.BUY)
                .customerId(1L)
                .assetName("BTC")
                .price(10L)
                .size(2L)
                .build();

        CustomerEntity customerEntity = CustomerEntity.builder().id(1L).amount(100L).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(orderMapper.orderToOrderEntity(any())).thenReturn(new OrderEntity());
        when(orderRepository.save(any())).thenReturn(new OrderEntity());
        when(orderMapper.orderEntityToOrder(any())).thenReturn(order);

        Order result = orderService.createOrder(order, mockPrincipal());

        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(80L, customerEntity.getAmount());
    }

    private UsernamePasswordAuthenticationToken mockPrincipal() {
        return new UsernamePasswordAuthenticationToken("user", null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
