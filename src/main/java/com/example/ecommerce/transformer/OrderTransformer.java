package com.example.ecommerce.transformer;

import com.example.ecommerce.Enum.OrderStatus;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.model.*;
import com.example.ecommerce.validate.CardValidation;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.UUID;

@UtilityClass
public class OrderTransformer {

    public static Ordered cartToOrder(Cart cart, String cardNo){
        return Ordered.builder()
                .orderNo(String.valueOf(UUID.randomUUID()))
                .noOfItems(cart.getNumberOfItems())
                .totalOrderValue(cart.getCartTotal())
                .orderStatus(OrderStatus.PLACED)
                .cardUsed(cardNo)
                .build();

    }

    public static OrderResponse orderToOrderResponse(Ordered order){
        return OrderResponse.builder()
                .customerName(order.getCustomer().getName())
                .orderNo(order.getOrderNo())
                .noOfItems(order.getNoOfItems())
                .totalOrderValue(order.getTotalOrderValue())
                .orderDate(order.getOrderDate())
                .cardUsed(CardTransformer.maskedCard(order.getCardUsed()))
                .build();
    }


    public static Ordered directItemToOrder(Customer customer, Item item, String cardNo){
        return Ordered.builder()
                .orderNo(String.valueOf(UUID.randomUUID()))
                .noOfItems(1)
                .totalOrderValue(item.getProduct().getPrice()* item.getRequiredQuantity())
                .orderStatus(OrderStatus.PLACED)
                .cardUsed(cardNo)
                .customer(customer)
                .build();
    }
}
