package com.example.ecommerce.transformer;

import com.example.ecommerce.Enum.OrderStatus;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.model.*;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
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

        List<ItemResponse> itemResponseList= new ArrayList<>();
        for (Item item:order.getItems()){
            itemResponseList.add(ItemTransformer.itemToItemResponse(item));
        }

        OrderResponse orderResponse= OrderResponse.builder()
                .customerName(order.getCustomer().getName())
                .orderNo(order.getOrderNo())
                .noOfItems(order.getNoOfItems())
                .totalOrderValue(order.getTotalOrderValue())
                .orderDate(order.getOrderDate())
                .cardUsed(CardTransformer.maskedCard(order.getCardUsed()))
                .itemResponseList(itemResponseList)
                .build();
        return orderResponse;
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
