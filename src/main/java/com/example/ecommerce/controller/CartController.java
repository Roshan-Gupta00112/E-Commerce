package com.example.ecommerce.controller;


import com.example.ecommerce.dtos.request.CheckOutCartRequest;
import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.dtos.response.CartResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity addToCart(@RequestBody ItemRequest itemRequest){

        try {
            // We are adding Item in the Cart and for that we have to create an Item and then add that item
            // in our Cart. So the return type of this createItem function will be "Item"
            Item newItem= itemService.createItem(itemRequest);
            CartResponse cartResponse= cartService.saveToCart(itemRequest.getCustomerEmailId(), newItem);
            return new ResponseEntity(cartResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/checkout")
    public ResponseEntity checkOutCart(@RequestBody CheckOutCartRequest checkOutCartRequest){
        try {
            OrderResponse orderResponse= cartService.checkOutCart(checkOutCartRequest);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
