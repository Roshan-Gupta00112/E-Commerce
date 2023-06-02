package com.example.ecommerce.controller;

import com.example.ecommerce.dtos.request.DirectOrderRequest;
import com.example.ecommerce.dtos.response.CustomerResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.service.OrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderedService orderedService;

    @PostMapping("/place-order-directly")
    public ResponseEntity placedDirectOrder(@RequestBody DirectOrderRequest directOrderRequest){
        try{
            OrderResponse orderResponse= orderedService.directPlaceOrder(directOrderRequest);
            return new ResponseEntity(orderResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/cancel-order")
    public ResponseEntity cancelOrder(@RequestParam("email") String customerEmailId,
                                      @RequestParam("orderNo") String orderNo){
        try {
            String str= orderedService.cancelOrder(customerEmailId, orderNo);
            return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-all-orders-of-customer")
    public ResponseEntity getAllOrdersOfCustomer(@RequestParam("emailId") String emailId){
        try {
            List<OrderResponse> orderResponseList= orderedService.getAllOrdersOfCustomer(emailId);
            return new ResponseEntity(orderResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-five-recent-orders-of customer")
    public ResponseEntity getLatestFiveOrderOfCustomer(@RequestParam("emailId") String emailId) {
        try {
            List<OrderResponse> orderResponseList= orderedService.getLatestFiveOrderOfCustomer(emailId);
            return new ResponseEntity(orderResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders-having-max-order-value")
    public List<OrderResponse> highestOrderValue(){
        return orderedService.highestOrderValue();
    }

    @GetMapping("/customers-having-max-order-value")
    public List<CustomerResponse> detailsOfCustomerWithMaxOrderValue(){
        return orderedService.detailsOfCustomerWithMaxOrderValue();
    }

}
