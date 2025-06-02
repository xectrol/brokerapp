package com.brokerapp.controller;

import com.brokerapp.controller.OrderController;
import com.brokerapp.controller.dto.OrderDto;
import com.brokerapp.controller.resource.OrderResource;
import com.brokerapp.enums.OrderSide;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.mapper.OrderMapper;
import com.brokerapp.model.Order;
import com.brokerapp.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private IOrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    private UsernamePasswordAuthenticationToken principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = new UsernamePasswordAuthenticationToken(
                "admin", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() {
        OrderDto dto = OrderDto.builder()
                .assetName("BTC")
                .price(100L)
                .size(1L)
                .orderSide(OrderSide.BUY)
                .customerId(1L)
                .build();

        Order order = Order.builder()
                .assetName("BTC")
                .price(100L)
                .size(1L)
                .orderSide(OrderSide.BUY)
                .customerId(1L)
                .status(OrderStatus.PENDING)
                .build();

        OrderResource resource = OrderResource.builder()
                .assetName("BTC")
                .price(100L)
                .size(1L)
                .orderSide(OrderSide.BUY)
                .customerId(1L)
                .status(OrderStatus.PENDING)
                .build();

        when(orderMapper.orderDtoToOrder(dto)).thenReturn(order);
        when(orderService.createOrder(order, principal)).thenReturn(order);
        when(orderMapper.orderToOrderResource(order)).thenReturn(resource);

        OrderResource result = orderController.createOrder(dto, principal);

        assertEquals("BTC", result.getAssetName());
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void listOrders_shouldReturnList() {
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        Order order = Order.builder().assetName("ETH").build();
        OrderResource resource = OrderResource.builder().assetName("ETH").build();

        when(orderService.listOrders(customerId, startDate, endDate, principal)).thenReturn(List.of(order));
        when(orderMapper.orderToOrderResource(order)).thenReturn(resource);

        List<OrderResource> result = orderController.listOrders(customerId, startDate, endDate, principal);

        assertEquals(1, result.size());
        assertEquals("ETH", result.get(0).getAssetName());
    }

    @Test
    void cancelOrder_shouldCallService() {
        orderController.cancelOrder(1L, principal);
        verify(orderService, times(1)).cancelOrder(1L, principal);
    }

    @Test
    void matchOrder_shouldCallService() {
        orderController.matchOrder(2L, principal);
        verify(orderService, times(1)).matchOrder(2L, principal);
    }
}
