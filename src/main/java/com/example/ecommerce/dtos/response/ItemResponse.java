package com.example.ecommerce.dtos.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemResponse {

    String productName;

    double pricePerQuantity;

    int quantity;

    double totalPrice;

    Date addedOnCart;
}
