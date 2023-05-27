package com.example.ecommerce.controller;

import com.example.ecommerce.Enum.CardType;
import com.example.ecommerce.dtos.request.CustomerRequest;
import com.example.ecommerce.dtos.request.UpdateInfoUsingEmail;
import com.example.ecommerce.dtos.request.UpdateInfoUsingMobNo;
import com.example.ecommerce.dtos.response.CardResponse;
import com.example.ecommerce.dtos.response.CustomerResponse;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody CustomerRequest customerRequest){
        try {
            CustomerResponse customerResponse=customerService.addCustomer(customerRequest);
            return new ResponseEntity(customerResponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-customer-using-email")
    public ResponseEntity getCustomerUsingEmail(@RequestParam("email") String emailId){
        try {
            CustomerResponse customerResponse=customerService.getCustomerUsingEmail(emailId);
            return new ResponseEntity(customerResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-customers-using-card-type")
    public List<CustomerResponse> getAllCustomersUsingCardType(@RequestParam("cardType") CardType cardType){
        return customerService.getAllCustomersUsingCardType(cardType);
    }

    @GetMapping("/get-all")
    public List<CustomerResponse> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/all-cards-of-customer")
    public ResponseEntity getAllCards(@RequestParam("email") String emailId){
        try {
            List<CardResponse> cardResponseList= customerService.getAllCards(emailId);
            return new ResponseEntity(cardResponseList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update-using-email")
    public ResponseEntity updateInfoByEmail(@RequestBody UpdateInfoUsingEmail updateInfoUsingEmail){
        try {
            CustomerResponse customerResponse= customerService.updateInfoByEmail(updateInfoUsingEmail);
            return new ResponseEntity(customerResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update-using-mobNo")
    public ResponseEntity updateInfoByMobNo(@RequestBody UpdateInfoUsingMobNo updateInfoUsingMobNo){
        try {
            CustomerResponse customerResponse= customerService.updateInfoByMobNo(updateInfoUsingMobNo);
            return new ResponseEntity(customerResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity deleteCustomer(@RequestParam("email") String emailId, @RequestParam("mobNo") String mobNo){

        try {
            String str= customerService.deleteCustomer(emailId, mobNo);
            return new ResponseEntity(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-either-by-email-or-by-mobNo")
    public ResponseEntity deleteCustomerEitherBYEmailIdOrByMobNo(@RequestParam("email") String emailId,
                                                                 @RequestParam("mobNo") String mobNo){
        try {
            String str= customerService.deleteCustomerEitherBYEmailIdOrByMobNo(emailId, mobNo);
            return new ResponseEntity<>(str, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
