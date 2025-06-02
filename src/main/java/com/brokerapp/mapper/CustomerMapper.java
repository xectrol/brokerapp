package com.brokerapp.mapper;


import com.brokerapp.controller.dto.CustomerDto;
import com.brokerapp.controller.dto.DepositDto;
import com.brokerapp.controller.dto.WithdrawDto;
import com.brokerapp.controller.resource.CustomerResource;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Deposit;
import com.brokerapp.model.Withdraw;
import com.brokerapp.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper( CustomerMapper.class );

    CustomerEntity customerToCustomerEntity(Customer customer);
    Customer customerEntityToCustomer(CustomerEntity customerEntity);
    Customer customerCommandToCustomer(CustomerDto customerDto);
    CustomerResource customerToCustomerResource(Customer customer);

    Deposit depositCommandToDeposit(DepositDto depositDto);
    Withdraw withdrawCommandToDeposit(WithdrawDto withdrawDto);

}
