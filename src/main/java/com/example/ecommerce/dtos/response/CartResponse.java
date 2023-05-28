package com.example.ecommerce.dtos.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartResponse {

    String customerName;

    Integer numberOfItems;

    double cartTotal;

    //List<Item> itemList;
    List<ItemResponse> itemResponseList;


}
