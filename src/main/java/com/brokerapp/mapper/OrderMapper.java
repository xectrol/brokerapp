package com.brokerapp.mapper;


import com.brokerapp.controller.dto.OrderDto;
import com.brokerapp.controller.resource.OrderResource;
import com.brokerapp.model.Order;
import com.brokerapp.persistence.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );

    Order orderDtoToOrder(OrderDto orderDto);

    OrderResource orderToOrderResource(Order order);

    OrderEntity orderToOrderEntity(Order order);

    Order orderEntityToOrder(OrderEntity orderEntity);
}
