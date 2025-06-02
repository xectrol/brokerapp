package com.brokerapp.controller;


import com.brokerapp.controller.dto.OrderDto;
import com.brokerapp.controller.resource.OrderResource;
import com.brokerapp.mapper.OrderMapper;
import com.brokerapp.model.Order;
import com.brokerapp.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final OrderMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResource createOrder(@RequestBody OrderDto orderDto, UsernamePasswordAuthenticationToken principal) {
        Order createdOrder = orderService.createOrder(mapper.orderDtoToOrder(orderDto), principal);
        return mapper.orderToOrderResource(createdOrder);
    }

    @GetMapping
    public List<OrderResource> listOrders(@RequestParam(name = "customerId", required = false) Long customerId,
                                          @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                          @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                          UsernamePasswordAuthenticationToken principal) {
        List<Order> orders = orderService.listOrders(customerId, startDate, endDate, principal);
        return orders.stream().map(mapper::orderToOrderResource).toList();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable(name = "id") Long id,
                            UsernamePasswordAuthenticationToken principal) {
        orderService.cancelOrder(id, principal);
    }
    @PostMapping("/match/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void matchOrder(@PathVariable(name = "orderId") Long orderId, UsernamePasswordAuthenticationToken principal) {
        orderService.matchOrder(orderId, principal);
    }
}
