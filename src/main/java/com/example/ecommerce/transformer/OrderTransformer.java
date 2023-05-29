package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.response.OrderResponse;
import com.example.ecommerce.model.Ordered;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderTransformer {

    public static OrderResponse orderToOrderResponse(Ordered order){
        return OrderResponse.builder()
                .customerName(order.getCustomer().getName())
                .orderNo(order.getOrderNo())
                .noOfItems(order.getNoOfItems())
                .totalOrderValue(order.getTotalOrderValue())
                .orderDate(order.getOrderDate())
                .cardUsed(order.getCardUsed())
                .build();
    }
}
