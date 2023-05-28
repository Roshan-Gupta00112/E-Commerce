package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.response.CartResponse;
import com.example.ecommerce.model.Cart;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CartTransformer {

    public static CartResponse cartToCartResponse(Cart cart){
        return CartResponse.builder()
                .customerName(cart.getCustomer().getName())
                .numberOfItems(cart.getNumberOfItems())
                .cartTotal(cart.getCartTotal())
                .build();
    }
}
