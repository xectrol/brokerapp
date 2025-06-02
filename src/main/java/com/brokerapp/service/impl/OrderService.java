package com.brokerapp.service.impl;

import com.brokerapp.constant.Constant;
import com.brokerapp.enums.OrderSide;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.exception.BadRequestException;
import com.brokerapp.exception.InsufficientBalanceException;
import com.brokerapp.exception.NotFoundException;
import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.mapper.OrderMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Order;
import com.brokerapp.persistence.AssetRepository;
import com.brokerapp.persistence.CustomerRepository;
import com.brokerapp.persistence.OrderRepository;
import com.brokerapp.persistence.entity.CustomerEntity;
import com.brokerapp.persistence.entity.OrderEntity;
import com.brokerapp.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final AssetService assetService;
    private final OrderMapper orderMapper;
    private final AssetMapper assetMapper;
    private final CustomerService customerService;

    public Order createOrder(Order order, UsernamePasswordAuthenticationToken principal) {

        if (order.getOrderSide().equals(OrderSide.SELL)) {
            Asset asset = assetService.assetsByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
            validateAsset(order, asset);
            asset.setUsableSize(asset.getUsableSize() - order.getSize());
            assetRepository.save(assetMapper.assetToAssetEntity(asset));
        } else if (order.getOrderSide().equals(OrderSide.BUY)) {
            Optional<CustomerEntity> customer = customerRepository.findById(order.getCustomerId());
            if (customer.isPresent()) {
                if (customer.get().getAmount() < order.totalAmount()) {
                    throw new InsufficientBalanceException("Customer does not have enough balance");
                }else {
                    customer.get().setAmount(customer.get().getAmount() - order.totalAmount());
                    customerRepository.save(customer.get());
                }
            }
        }
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());

        return orderMapper.orderEntityToOrder(orderRepository.save(orderMapper.orderToOrderEntity(order)));
    }

    public void matchOrder(Long orderId, UsernamePasswordAuthenticationToken principal) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new InsufficientBalanceException("Order Id do not have this system"));
        Order order = orderMapper.orderEntityToOrder(orderEntity);

        if (!orderEntity.getStatus().equals(OrderStatus.PENDING)) {
            throw new BadRequestException("Only pending orders can be MATCHED");
        }

        Optional<CustomerEntity> customer = customerRepository.findById(order.getCustomerId());

        if (order.getOrderSide() == OrderSide.SELL) {
            Asset asset = assetService.assetsByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
            asset.setSize(asset.getSize() - order.getSize());
            assetRepository.save(assetMapper.assetToAssetEntity(asset));
            customer.get().setAmount(customer.get().getAmount() + order.totalAmount());
            customerRepository.save(customer.get());
        } else if (order.getOrderSide() == OrderSide.BUY) {
            if (assetService.assetsByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName()) == null) {
                 Asset asset = new Asset();
                 asset.setSize(order.getSize());
                 asset.setUsableSize(order.getSize());
                 asset.setAssetName(order.getAssetName());
                 asset.setCustomerId(order.getCustomerId());
                 assetRepository.save(assetMapper.assetToAssetEntity(asset));
            } else {
                Asset asset = assetService.assetsByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
                asset.setSize(asset.getSize() + order.getSize());
                asset.setUsableSize(asset.getUsableSize() + order.getSize());
                assetRepository.save(assetMapper.assetToAssetEntity(asset));
            }
        }

        order.setStatus(OrderStatus.MATCHED);
        orderRepository.save(orderMapper.orderToOrderEntity(order));
    }

    private static void validateAsset(Order order, Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException(String.format("Customer do not have '%s' asset", order.getAssetName()));
        }

        if (!asset.getAssetName().equals(order.getAssetName())) {
            throw new NotFoundException("Customer does not have the specified asset: " + order.getAssetName());
        }

        if (order.getSize() > asset.getUsableSize()) {
            throw new IllegalArgumentException("Customer do not have enough sellable asset");
        }

    }

    public List<Order> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate, UsernamePasswordAuthenticationToken principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        if (!customer.getRole().equals(Constant.ROLE_ADMIN)) {
            customerId = customer.getId();
        }
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate).stream().map(orderMapper::orderEntityToOrder).toList();
    }

    public void cancelOrder(Long orderId, UsernamePasswordAuthenticationToken principal) {

        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new InsufficientBalanceException("Order Id do not have this system"));
        if (!orderEntity.getStatus().equals(OrderStatus.PENDING)) {
            throw new BadRequestException("Only pending orders can be cancelled");
        }
        Order order = orderMapper.orderEntityToOrder(orderEntity);

        Asset asset = assetService.assetsByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());

        if (order.getOrderSide() == OrderSide.SELL) {
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
            assetRepository.save(assetMapper.assetToAssetEntity(asset));
        } else if (order.getOrderSide() == OrderSide.BUY) {
            Optional<CustomerEntity> customer = customerRepository.findById(order.getCustomerId());
            customer.get().setAmount(customer.get().getAmount() + order.totalAmount());
            customerRepository.save(customer.get());

        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(orderMapper.orderToOrderEntity(order));

    }

}

