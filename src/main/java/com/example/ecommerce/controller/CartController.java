package com.example.ecommerce.controller;


import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.dtos.response.CartResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    public CartResponse addToCart(@RequestBody ItemRequest itemRequest){

    }
}
