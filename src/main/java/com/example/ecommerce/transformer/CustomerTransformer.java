package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.request.CustomerRequest;
import com.example.ecommerce.dtos.response.CustomerResponse;
import com.example.ecommerce.model.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerTransformer {

    public static Customer customerRequestToCustomer(CustomerRequest customerRequest){
        return Customer.builder()
                .name(customerRequest.getName())
                .dob(customerRequest.getDob())
                .emailId(customerRequest.getEmailId())
                .mobNo(customerRequest.getMobNo())
                .address(customerRequest.getAddress())
                .build();
    }

    public static CustomerResponse customerTocustomerResponse(Customer customer){
        return CustomerResponse.builder()
                .name(customer.getName())
                .dob(customer.getDob())
                .emailId(customer.getEmailId())
                .mobNo(customer.getMobNo())
                .address(customer.getAddress())
                .build();
    }
}
