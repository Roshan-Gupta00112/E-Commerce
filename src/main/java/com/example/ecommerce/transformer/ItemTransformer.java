package com.example.ecommerce.transformer;

import com.example.ecommerce.dtos.request.DirectOrderRequest;
import com.example.ecommerce.dtos.request.ItemRequest;
import com.example.ecommerce.dtos.response.ItemResponse;
import com.example.ecommerce.model.Item;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemTransformer {

    public static Item itemRequestToItem(ItemRequest itemRequest){
        return Item.builder()
                .requiredQuantity(itemRequest.getRequiredQuantity())
                .build();
    }


    public static ItemResponse itemToItemResponse(Item item){
        return ItemResponse.builder()
                .productName(item.getProduct().getName())
                .pricePerQuantity(item.getProduct().getPrice())
                .quantity(item.getRequiredQuantity())
                .totalPrice(item.getRequiredQuantity()*item.getProduct().getPrice())
                .build();
    }

    public static ItemRequest directOrderRequestToItemRequest(DirectOrderRequest directOrderRequest){
        return ItemRequest.builder()
                .customerEmailId(directOrderRequest.getCustomerEmailId())
                .productId(directOrderRequest.getProductId())
                .requiredQuantity(directOrderRequest.getRequiredQuantity())
                .build();
    }
}
